package bluehack.table9.medical.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

/**
 * A note for doctors
 */
@Document(collection = "note")
public class NoteEntity implements Serializable {

    @Id
    private String id;

    private String note;

    @Indexed
    private String patientId;

    private String diseaseCode;

    private String nodeDate;

    private ArrayList<String[]> problem;

    private ArrayList<String[]> treatment;

    private ArrayList<String[]> test;

    public String getNodeDate() {
        return nodeDate;
    }

    public void setNodeDate(String nodeDate) {
        this.nodeDate = nodeDate;
    }

    public ArrayList<String[]> getProblem() {
        return problem;
    }

    public void setProblem(ArrayList<String[]> problem) {
        this.problem = problem;
    }

    public ArrayList<String[]> getTreatment() {
        return treatment;
    }

    public void setTreatment(ArrayList<String[]> treatment) {
        this.treatment = treatment;
    }

    public ArrayList<String[]> getTest() {
        return test;
    }

    public void setTest(ArrayList<String[]> test) {
        this.test = test;
    }

    private Instant timestamp = Instant.now();

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
