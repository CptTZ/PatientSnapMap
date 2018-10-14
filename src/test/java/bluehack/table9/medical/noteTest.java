package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class noteTest {

    @Autowired
    private NoteDao noteDao;

    @Test
    public void parseNoteTest() throws Exception {
        for (int i = 1; i <= 8; i++) {
            parseNode(i);
        }
    }

    private void parseNode(int i) throws Exception {
        String f1 = String.format("text/seizure%d.con", i);
        String f2 = String.format("text/seizure%d.txt", i);

        List<String> conData = Files.readAllLines(Paths.get(f1));
        List<String> originalData = Files.readAllLines(Paths.get(f2));

        NoteEntity n = extractNlpResult(originalData, conData);
        n.setNote(String.join("\r\n", originalData));

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

            System.err.print(whichLine + 1 + ": ");
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

        System.err.println(String.format("%d\t%d\t%s%n", left + 1, right + 1, origLine.substring(left, right)));
        return new int[]{left, right};
    }

}
