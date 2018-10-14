package bluehack.table9.medical.model.dao;

import bluehack.table9.medical.model.dto.PatientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class PatientDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void addPatient(PatientEntity p) {
        this.mongoTemplate.save(p);
    }

    public PatientEntity findPatientByPid(String pid) {
        return this.mongoTemplate.findById(pid, PatientEntity.class);
    }

    public void removePatientById(String pid) {
        PatientEntity p = new PatientEntity();
        p.setPatientId(pid);
        this.mongoTemplate.remove(p);
    }

}
