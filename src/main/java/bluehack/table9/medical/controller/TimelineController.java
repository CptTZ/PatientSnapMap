package bluehack.table9.medical.controller;

import bluehack.table9.medical.controller.vo.TimelineBean;
import bluehack.table9.medical.model.dto.NoteEntity;
import bluehack.table9.medical.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Main page for timeline
 */
@Controller
public class TimelineController {

    @Autowired
    private TimelineService timelineService;

    @GetMapping(value = "api/timeline/{pid}")
    @ResponseBody
    public List<TimelineBean> getDataHandler(@PathVariable String pid) {
        pid = pid.toUpperCase();

        List<NoteEntity> notes = timelineService.getPatientAllOriginalNote(pid);
        List<String> orig = timelineService.getPatientAllOriginalNotes(pid);
        List<String> test = timelineService.getPatientAllTestMarkedNotes(pid);
        List<String> prob = timelineService.getPatientAllProblemMarkedNotes(pid);
        List<String> trea = timelineService.getPatientAllTreatmentMarkedNotes(pid);

        ArrayList<Integer> totalFreq = new ArrayList<>(notes.size());
        for (int i = 0; i < notes.size(); i++) {
            String diseaseName = notes.get(i).getDiseaseCode();
            String[] tmp = diseaseName.split(" ");
            String diseaseFreqTerm = tmp[tmp.length - 1];
            totalFreq.add(countOccurences(orig.get(i), diseaseFreqTerm));
        }

        int sumTotalFreq = 0;
        for (int i : totalFreq) {
            sumTotalFreq += i;
        }

        List<TimelineBean> tb = new ArrayList<>(notes.size());
        for (int i = 0; i < notes.size(); i++) {
            TimelineBean timelineBean = new TimelineBean();
            timelineBean.setFrequency(1.0 * totalFreq.get(i) / sumTotalFreq);
            timelineBean.setFullText(orig.get(i));
            timelineBean.setProblem(prob.get(i));
            timelineBean.setTest(test.get(i));
            timelineBean.setTreatment(trea.get(i));
            timelineBean.setTitle(String.format("Note for '%s'", notes.get(i).getDiseaseCode()));
            SimpleDateFormat format0 = new SimpleDateFormat("MMM dd yyyy");
            timelineBean.setTime(format0.format(notes.get(i).getNoteDate()));
            tb.add(timelineBean);
        }
        return tb;
    }

    private int countOccurences(String str, String word) {
        word = word.toLowerCase();
        String a[] = str.toLowerCase().split(" ");

        int count = 0;
        for (String anA : a) {
            if (word.equals(anA)) count++;
        }

        return count;
    }

}
