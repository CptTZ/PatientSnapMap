package bluehack.table9.medical.service;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dao.PatientDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<String> getPatientAllOriginalNotes(String pid) {
        populateCurrentNotes(pid);
        ArrayList<String> notesList = new ArrayList<>(this.cacheCurrentPatientNote.size());

        for (NoteEntity n : this.cacheCurrentPatientNote) {
            notesList.add(n.getNote().replace("\r\n", "</br>"));
        }

        return notesList;
    }

    private void populateCurrentNotes(String pid) {
        this.cacheCurrentPatientNote = noteDao.findNotesByPatientIdDesc(pid);
    }

}
