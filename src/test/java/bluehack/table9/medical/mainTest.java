package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dto.NoteEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class mainTest {

    @Autowired
    private NoteDao noteDao;

    @Test
    public void mainA() throws Exception {
        List<NoteEntity> ne = this.noteDao.findNotesByPatientIdDesc("IBM-2018-001");
        System.err.println(ne.size());
    }

}
