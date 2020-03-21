package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login";
    }

    @GetMapping("/login/error")
    public String loginPageGet(Model model) {
        model.addAttribute("error", true);
        return "login";

    }



    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

}
