package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Service.UserRegister;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
public class GuestController {

    private final UserRepository<User> userRepository;
    private final UserRegister userService;


    @GetMapping("/")
    public String mainPage() {
        return "index";
    }


    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "security/register";
    }


    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user, Model model) {
        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();


        if (!password.equals(repeatPassword)) {
            model.addAttribute("errorPassword", true);
            return "security/register";
        }
        model.addAttribute("registerSuccess", true);
        user.setRole(Role.USER);
        userService.registerNewUser(user);
        return "users/guest/index";
    }
    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_MANAGER")) {
            return "redirect:/manager/dashboard";
        } else if (request.isUserInRole("ROLE_ADMIN")) {

            System.out.println("PANEL ADMINA SIE ZGADZA");

            return "redirect:/";
        }

        return "redirect:/";

    }


}