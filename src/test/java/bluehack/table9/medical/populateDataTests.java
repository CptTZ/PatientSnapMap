package bluehack.table9.medical;

import bluehack.table9.medical.model.dao.NoteDao;
import bluehack.table9.medical.model.dao.PatientDao;
import bluehack.table9.medical.model.dao.UserDao;
import bluehack.table9.medical.model.dto.PatientEntity;
import bluehack.table9.medical.model.dto.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class populateDataTests {

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PatientDao patientDao;

    /**
     * Add a user for this system
     */
    @Test
    public void addUserTest() {
        UserEntity u = new UserEntity();
        u.setUsername("table9");
        u.setPasswordHash("admin");
        u.setAge(23);
        u.setFirstname("Blue");
        u.setLastname("Hack");
        this.userDao.addUser(u);
    }

    /**
     * Add a patient for this system
     */
    @Test
    public void addPatientTest() {
        Random r = new Random();
        for (int i = 1; i <= 8; i++) {
            PatientEntity p = new PatientEntity();
            p.setAge(r.nextInt(80));
            p.setGender((byte) (r.nextInt(10) % 2 == 0 ? 1 : 0));
            p.setName(String.format("Blue Hack - %d", i));
            p.setPatientId(String.format("IBM-2018-%03d", i));
            this.patientDao.addPatient(p);
        }
    }

}
