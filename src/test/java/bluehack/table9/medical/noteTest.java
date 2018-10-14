package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class noteTest {

    @Autowired
    private NoteDao noteDao;

    @Test
    public void parseNote1Test() throws Exception {
        for (int i = 1; i <= 8; i++) {
            parseNote(String.format("seizure%d", i), "IBM-2018-001");
        }
    }

    @Test
    public void parseNote2Test() throws Exception {
        parseNote("testnote", "IBM-2018-002");
    }

    private void parseNote(String filename, String pid) throws Exception {
        String f1 = String.format("text/%s.con", filename);
        String f2 = String.format("text/%s.txt", filename);

        List<String> conData = Files.readAllLines(Paths.get(f1));
        List<String> originalData = Files.readAllLines(Paths.get(f2));

        String[] info = extractInfo(f2);

        NoteEntity n = extractNlpResult(originalData, conData);
        n.setNote(String.join("\r\n", originalData));
        n.setNoteDate(new SimpleDateFormat("MM/dd/yyyy").parse(info[0].trim()));
        n.setDiseaseCode(info[1]);
        System.err.println(String.format("Date: %s\tDisease: %s%n", info[0], info[1]));
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
            int whichLine = Integer.parseInt(d[1]) - 1,
                    startPos = Integer.parseInt(d[2]),
                    endPos = Integer.parseInt(d[3]);
            String tipText = d[4].trim();

            int[] sePos = calcStartEndPosition(originalData.get(whichLine), startPos, endPos, tipText.toLowerCase());

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

    private int[] calcStartEndPosition(String origLine, int startTokenPos, int endTokenPos, String tip) {
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
