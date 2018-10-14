package bluehack.table9.medical.model.dao;

import bluehack.table9.medical.model.dto.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveNote(NoteEntity note) {
        this.mongoTemplate.save(note);
    }

    public void removeNoteById(String id) {
        NoteEntity n = new NoteEntity();
        n.setId(id);
        this.mongoTemplate.remove(n);
    }

    public NoteEntity findNoteById(String id) {
        return mongoTemplate.findById(id, NoteEntity.class);
    }

    public List<NoteEntity> findNotesByPatientId(String pid) {
        Query query = new Query(Criteria.where("patientId").is(pid));
        return mongoTemplate.find(query, NoteEntity.class);
    }

}
