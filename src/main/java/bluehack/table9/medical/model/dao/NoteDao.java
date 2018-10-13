package bluehack.table9.medical.model.dao;

import bluehack.table9.medical.model.dto.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


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
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, NoteEntity.class);
    }

}
