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
        List<String> conData = Files.readAllLines(Paths.get("seizure1.con"));
        List<String> originalData = Files.readAllLines(Paths.get("seizure1.txt"));

        NoteEntity n = extractInfo(conData);
        n.setNote(String.join("\r\n", originalData));

        this.noteDao.saveNote(n);
    }

    private NoteEntity extractInfo(List<String> conData) {
        NoteEntity n = new NoteEntity();

        ArrayList<String[]> problem = new ArrayList<>();
        ArrayList<String[]> treat = new ArrayList<>();
        ArrayList<String[]> test = new ArrayList<>();

        for (String line : conData) {
            String[] d = line.split(",");
            String[] dd = new String[]{d[1], d[2]};
            switch (d[0]) {
                case "problem":
                    problem.add(dd);
                    break;
                case "test":
                    test.add(dd);
                    break;
                case "treatment":
                    treat.add(dd);
                    break;
            }
        }

        n.setTest(test);
        n.setTreatment(treat);
        n.setProblem(problem);
        return n;
    }

}
