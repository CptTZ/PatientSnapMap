package bluehack.table9.medical.service;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bluehack.table9.medical.MedicalApplication.TMP_PATH;

@Service
public class NoteService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NoteDao noteDao;

    public void backendProcessFile(String noteData, String pid) {
        String rndFileName = UUID.randomUUID().toString();
        String tmpFilePath = String.format("%s/%s.txt", TMP_PATH, rndFileName);
        String resFilePath = String.format("%s/%s.con", TMP_PATH, rndFileName);
        logger.info("Note file path: {}", tmpFilePath);

        try {
            writeOutFile(noteData, tmpFilePath);
            pythonRunner(tmpFilePath);
            parseNote(resFilePath, tmpFilePath, pid);
        } catch (Exception e) {
            logger.error("Run NLP failed", e);
        }

    }

    private void writeOutFile(String data, String filepath) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filepath)));
        bw.write(data);
        bw.close();
    }

    private boolean pythonRunner(String tmpFilePath) throws Exception {
        String pathTemplate = "/home/tonyz/anaconda3/envs/bluehack/bin/python /home/tonyz/Desktop/CliNER/cliner predict --txt %s --out %s --model /home/tonyz/Desktop/CliNER/models/silver.crf --format i2b2";
        Process p = Runtime.getRuntime().exec(String.format(pathTemplate, tmpFilePath, TMP_PATH));
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    // Below are method to parse the output of NLP program

    /**
     * @param f1  NLP result file path
     * @param f2  Original note file path
     * @param pid PatientID
     * @throws Exception
     */
    private void parseNote(String f1, String f2, String pid) throws Exception {
        List<String> conData = Files.readAllLines(Paths.get(f1));
        List<String> originalData = Files.readAllLines(Paths.get(f2));

        String[] info = extractInfo(f2);

        NoteEntity n = extractNlpResult(originalData, conData);
        n.setNote(String.join("\r\n", originalData));
        n.setNoteDate(new SimpleDateFormat("MM/dd/yyyy").parse(info[0].trim()));
        n.setDiseaseCode(info[1]);
        logger.info(String.format("Date: %s\tDisease: %s%n", info[0], info[1]));
        n.setPatientId(pid);

        this.noteDao.saveNote(n);
    }

    private NoteEntity extractNlpResult(List<String> originalData, List<String> conData) {
        NoteEntity n = new NoteEntity();

        ArrayList<String[]> problem = new ArrayList<>();
        ArrayList<String[]> treat = new ArrayList<>();
        ArrayList<String[]> test = new ArrayList<>();

        for (String line : conData) {
            String[] d = line.split("\t");
            // NLP starts with 1
            int whichLine = Integer.parseInt(d[1]) - 1;
            String tipText = d[4].trim();

            int[] sePos = calcStartEndPosition(originalData.get(whichLine), tipText.toLowerCase());

            String[] tmp = new String[]{String.valueOf(whichLine), String.valueOf(sePos[0]), String.valueOf(sePos[1]), tipText};
            switch (d[0]) {
                case "problem":
                    problem.add(tmp);
                    break;
                case "test":
                    test.add(tmp);
                    break;
                case "treatment":
                    treat.add(tmp);
                    break;
            }
        }

        n.setTest(test);
        n.setTreatment(treat);
        n.setProblem(problem);
        return n;
    }

    private int[] calcStartEndPosition(String origLine, String tip) {
        tip = tip.replace("   ", "\t");
        tip = tip.replace("__num__", "");
        int left = origLine.toLowerCase().indexOf(tip);
        int right = left + tip.length();

        origLine.substring(left, right);
        return new int[]{left, right};
    }

    private String[] extractInfo(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;

        Pattern datePt = Pattern.compile("[0-9]?[0-9]?\\/[0-9]?[0-9]?\\/2...");
        Pattern diagPt = Pattern.compile(".*(ICD10-CM .*, Working, Diagnosis).");

        String date = "Unknown", diag = "Unknown";

        while ((str = br.readLine()) != null) {
            //get the date for the note
            Matcher dateMt = datePt.matcher(str);
            if (dateMt.find()) {
                date = dateMt.group(0).trim();
            }
            // get the diagnosis for the note
            Matcher diagMt = diagPt.matcher(str);

            if (diagMt.find()) {
                String diagTerm = diagMt.group(0).replace(", Working, Diagnosis\\).", "").split(" \\(")[0];
                diag = diagTerm.replace("Diagnosis:", "").trim();
            }
        }
        br.close();

        return new String[]{date, diag};
    }

}
