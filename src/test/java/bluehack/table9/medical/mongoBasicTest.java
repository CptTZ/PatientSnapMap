package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dao.UserDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import bluehack.table9.medical.model.dto.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class mongoBasicTest {

    private String uuid = UUID.randomUUID().toString();
    private String username = "admin";
    private String password = "table9";

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void userSaveLoadTest() throws Exception {
        System.err.println(uuid);

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
        System.err.println(uuid);
        String noteData = String.join("\r\n", Files.readAllLines(Paths.get("testnote.txt")));

        NoteEntity n = new NoteEntity();
        n.setId(uuid);
        n.setNote(noteData);
        n.setPatientId("PUH-2018-011");
        this.noteDao.saveNote(n);

        NoteEntity load = this.noteDao.findNoteById(uuid);
        assertEquals(load.getPatientId(), "PUH-2018-011");
        assertEquals(load.getNote(), noteData);

        this.noteDao.removeNoteById(uuid);

        NoteEntity removed = this.noteDao.findNoteById(uuid);
        assertNull(removed);
    }
}
