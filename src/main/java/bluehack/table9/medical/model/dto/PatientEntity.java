package bluehack.table9.medical.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

/**
 * Patient data
 */
@Document(collection = "patient")
public class PatientEntity implements Serializable {

    @Id
    private String patientId;

    private String name;

    private int age;

    /**
     * 0: M, 1:F
     */
    private byte gender;

    private Instant timestamp = Instant.now();

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }
}
