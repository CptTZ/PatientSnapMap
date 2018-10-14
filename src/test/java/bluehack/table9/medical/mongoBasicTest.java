package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dao.PatientDao;
import bluehack.table9.medical.model.dao.UserDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import bluehack.table9.medical.model.dto.PatientEntity;
import bluehack.table9.medical.model.dto.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class mongoBasicTest {

    private String username = "admin";
    private String password = "table9";
    private String pid = "PUH-2019-011";

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PatientDao patientDao;

    @Test
    public void patientTest() throws Exception {
        PatientEntity p = new PatientEntity();
        p.setAge(18);
        p.setGender((byte) 0);
        p.setName("Blue Hack Jr");
        p.setPatientId(this.pid);
        this.patientDao.addPatient(p);

        PatientEntity pp = this.patientDao.findPatientByPid(this.pid);
        assertEquals(pp.getPatientId(), this.pid);
        assertEquals(pp.getAge(), 18);
        assertEquals(pp.getName(), "Blue Hack Jr");

        this.patientDao.removePatientById(this.pid);
        assertNull(this.patientDao.findPatientByPid(this.pid));
    }

    @Test
    public void userSaveLoadTest() throws Exception {
        UserEntity u = new UserEntity();
        u.setUsername(this.username);
        u.setPasswordHash(this.password);
        this.userDao.addUser(u);

        assertTrue(this.userDao.userLoginCheck(this.username, this.password));
        assertFalse(this.userDao.userLoginCheck(this.username, "admin"));

        this.userDao.removeUserByUsername(this.username);
        assertFalse(this.userDao.userLoginCheck(this.username, this.password));
    }

    @Test
    public void noteSaveLoadTest() throws Exception {
        String noteData = String.join("\r\n", Files.readAllLines(Paths.get("testnote.txt")));

        LinkedList<String> uuids = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            uuids.add(saveOneNoteForFakePid(noteData));
        }

        NoteEntity load = this.noteDao.findNoteById(uuids.get(3));
        assertEquals(load.getPatientId(), this.pid);
        assertEquals(load.getNote(), noteData);

        assertEquals(5, this.noteDao.findNotesByPatientId(this.pid).size());

        for (String uuid : uuids) {
            this.noteDao.removeNoteById(uuid);
            assertNull(this.noteDao.findNoteById(uuid));
        }
        assertEquals(0, this.noteDao.findNotesByPatientId(this.pid).size());
    }

    private String saveOneNoteForFakePid(String data) {
        String uuid = UUID.randomUUID().toString();
        NoteEntity n = new NoteEntity();
        n.setId(uuid);
        n.setNote(data);
        n.setPatientId(this.pid);
        this.noteDao.saveNote(n);
        return uuid;
    }

}
