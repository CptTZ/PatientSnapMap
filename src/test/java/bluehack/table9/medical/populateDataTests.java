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
        PatientEntity p;

        p = new PatientEntity();
        p.setAge(18);
        p.setGender((byte) 0);
        p.setName("Blue Hack Jr");
        p.setPatientId("PUH-2017-935");
        this.patientDao.addPatient(p);

        p = new PatientEntity();
        p.setAge(25);
        p.setGender((byte) 1);
        p.setName("Blue Hack");
        p.setPatientId("PUH-2007-935");
        this.patientDao.addPatient(p);
    }

}
