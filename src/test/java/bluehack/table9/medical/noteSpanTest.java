package bluehack.table9.medical;

import bluehack.table9.medical.service.TimelineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class noteSpanTest {

    @Autowired
    private TimelineService ts;

    @Test
    public void problemTest() throws Exception {
        String pid = "IBM-2018-001";
        this.ts.getPatientAllOriginalNotes(pid);
        this.ts.getPatientName(pid);
        this.ts.getPatientAllProblemMarkedNotes(pid);
        this.ts.getPatientAllTestMarkedNotes(pid);
        this.ts.getPatientAllTreatmentMarkedNotes(pid);
    }

}
