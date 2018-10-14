package bluehack.table9.medical.service;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dao.PatientDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Data to form timeline
 */
@Service
public class TimelineService {

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private PatientDao patientDao;

    private List<NoteEntity> cacheCurrentPatientNote;

    public String getPatientName(String pid) {
        return this.patientDao.findPatientByPid(pid).getName();
    }

    public List<NoteEntity> getPatientAllOriginalNote(String pid) {
        return noteDao.findNotesByPatientIdDesc(pid);
    }

    public List<String> getPatientAllOriginalNotes(String pid) {
        populateCurrentNotes(pid);
        ArrayList<String> notesList = new ArrayList<>(this.cacheCurrentPatientNote.size());

        for (NoteEntity n : this.cacheCurrentPatientNote) {
            notesList.add(n.getNote().replace("\r\n", "</br>"));
        }

        return notesList;
    }


    public List<String> getPatientAllProblemMarkedNotes(String pid) {
        populateCurrentNotes(pid);
        ArrayList<String> htmlNotes = new ArrayList<>(this.cacheCurrentPatientNote.size());

        for (NoteEntity n : this.cacheCurrentPatientNote) {
            htmlNotes.add(processOneNoteType(n, "problem"));
        }

        return htmlNotes;
    }

    public List<String> getPatientAllTestMarkedNotes(String pid) {
        populateCurrentNotes(pid);
        ArrayList<String> htmlNotes = new ArrayList<>(this.cacheCurrentPatientNote.size());

        for (NoteEntity n : this.cacheCurrentPatientNote) {
            htmlNotes.add(processOneNoteType(n, "test"));
        }

        return htmlNotes;
    }

    public List<String> getPatientAllTreatmentMarkedNotes(String pid) {
        populateCurrentNotes(pid);
        ArrayList<String> htmlNotes = new ArrayList<>(this.cacheCurrentPatientNote.size());

        for (NoteEntity n : this.cacheCurrentPatientNote) {
            htmlNotes.add(processOneNoteType(n, "treatment"));
        }

        return htmlNotes;
    }

    private void populateCurrentNotes(String pid) {
        this.cacheCurrentPatientNote = noteDao.findNotesByPatientIdDesc(pid);
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
