package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class noteSpanTest {

    @Autowired
    private NoteDao noteDao;

    @Test
    public void problemTest() throws Exception {
        List<NoteEntity> ns = noteDao.findNotesByPatientIdDesc("IBM-2018-001");
        for (NoteEntity n : ns) {
            String s = processOneNoteType(n, "problem");
            System.err.println(s);
        }
        for (NoteEntity n : ns) {
            String s = processOneNoteType(n, "test");
            System.err.println(s);
        }
        for (NoteEntity n : ns) {
            String s = processOneNoteType(n, "treatment");
            System.err.println(s);
        }
    }

    private String processOneNoteType(NoteEntity n, String type) {
        HashMap<Integer, ArrayList<Integer[]>> lineNumCounter = new HashMap<>();
        ArrayList<String[]> targetLists;
        switch (type) {
            case "problem":
                targetLists = n.getProblem();
                break;
            case "test":
                targetLists = n.getTest();
                break;
            case "treatment":
                targetLists = n.getTreatment();
                break;
            default:
                targetLists = new ArrayList<>();
                break;
        }

        String[] noteLines = n.getNote().split("\r\n");

        StringBuilder sb = new StringBuilder();

        for (String[] targetData : targetLists) {
            int lineNum = Integer.parseInt(targetData[0]);
            ArrayList<Integer[]> s = lineNumCounter.getOrDefault(lineNum, new ArrayList<>());
            if (s.size() == 0) {
                lineNumCounter.put(lineNum, s);
            }
            s.add(new Integer[]{Integer.parseInt(targetData[1]), Integer.parseInt(targetData[2])});
        }

        for (int i = 0; i < noteLines.length; i++) {
            String lineData = noteLines[i];
            int lineDataLen = lineData.length();
            if (lineNumCounter.containsKey(i)) {
                ArrayList<Integer[]> segments = lineNumCounter.get(i);
                sb.append(lineData.substring(0, segments.get(0)[0]));
                int nextStart = 0;
                for (int j = 0; j < segments.size(); j++) {
                    int start = segments.get(j)[0], end = segments.get(j)[1];
                    String sub = lineData.substring(start, end);
                    sb.append(addTextSpan(sub, type));
                    if (j < segments.size() - 1)
                        nextStart = segments.get(j + 1)[0];
                    if (nextStart > end)
                        sb.append(lineData.substring(end, nextStart));
                }
                int lastSegIndex = segments.get(segments.size() - 1)[1];
                if (lastSegIndex != lineDataLen)
                    sb.append(lineData.substring(lastSegIndex, lineDataLen - 1));
            } else {
                sb.append(lineData);
            }
            sb.append("</br>");
        }

        return sb.toString();
    }

    private String addTextSpan(String segment, String type) {
        String startSpan = String.format("<span class='%s'> ", type);
        return startSpan + segment + " </span>";
    }

}
