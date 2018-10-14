package bluehack.table9.medical.controller;

import bluehack.table9.medical.controller.vo.RawNoteBean;
import bluehack.table9.medical.controller.vo.RestfulResponse;
import bluehack.table9.medical.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping(value = "api/note")
    @ResponseBody
    public RestfulResponse loginHandler(@RequestBody(required = true) RawNoteBean rn) {
        new Thread(() -> this.noteService.backendProcessFile(rn.getNote(), rn.getPid().toUpperCase())).start();
        return new RestfulResponse(1, "Success");
    }

}
