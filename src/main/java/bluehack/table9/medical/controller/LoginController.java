package bluehack.table9.medical.controller;

import bluehack.table9.medical.controller.vo.LoginBean;
import bluehack.table9.medical.controller.vo.RestfulResponse;
import bluehack.table9.medical.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping(value = "api/login")
    @ResponseBody
    public RestfulResponse loginHandler(@RequestBody(required = true) LoginBean lb) {
        if (loginService.isPidValid(lb.getPid())) {
            if (loginService.isLoginCredValid(lb.getUsername(), lb.getPassword())) {
                return new RestfulResponse(1, "Success");
            } else {
                return new RestfulResponse(0, "Invalid credential");
            }
        }
        return new RestfulResponse(0, "Invalid patient ID");
    }

}
