package bluehack.table9.medical.model.dao;

import bluehack.table9.medical.model.dto.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void addUser(UserEntity u) {
        mongoTemplate.save(u);
    }

    public void removeUserByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        UserEntity u = mongoTemplate.findOne(query, UserEntity.class);
        if (u != null)
            mongoTemplate.remove(u);
    }

    public boolean userLoginCheck(String username, String passHash) {
        Query query = new Query(Criteria.where("username").is(username));
        UserEntity u = mongoTemplate.findOne(query, UserEntity.class);
        if (u == null) return false;
        return u.getPasswordHash().equals(passHash);
    }

}
