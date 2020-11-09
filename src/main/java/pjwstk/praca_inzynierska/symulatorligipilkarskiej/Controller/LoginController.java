package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String getLogin(Model model) {

        return "security/login";
    }

    @GetMapping("/login/error")
    public String loginPageGet(Model model) {
        model.addAttribute("error", true);
        return "security/login";

    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        model.addAttribute("info", "Dostęp zabroniony!!!!");
        return "security/accessDenied";
    }




}
