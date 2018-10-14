package bluehack.table9.medical;

import bluehack.table9.medical.service.NoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class noteTest {

    @Autowired
    private NoteService noteService;

    @Test
    public void parseNote1Test() throws Exception {

    }

    private Runnable threadRunnable(String rawNote, String pid) {
        return () -> this.noteService.backendProcessFile(rawNote, pid);
    }

}
