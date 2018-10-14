package bluehack.table9.medical.service;

import bluehack.table9.medical.model.dao.PatientDao;
import bluehack.table9.medical.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private UserDao userDao;

    /**
     * Do we have this PID?
     */
    public boolean isPidValid(String pid) {
        return this.patientDao.findPatientByPid(pid) != null;
    }

    public boolean isLoginCredValid(String username, String passwd) {
        return this.userDao.userLoginCheck(username, passwd);
    }

}
