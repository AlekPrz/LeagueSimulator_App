package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Service.UserRegister;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

@Controller
public class UserController {
/*

    @Autowired
    private UserRegister userService;
    @Autowired
    private UserRepository<Manager> userUserRepository;


    @GetMapping
    public String registerGet(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "security/register";
    }



  @PostMapping
    public String registerPost(@ModelAttribute User user, Model model) {
        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();


        if (user.getRole().getDescription().equals("ROLE_MANAGER")) {


            Manager manager = Manager.builder().username(user.getUsername()).password(UserRegister.encodePassword(password))
                    .repeatPassword(UserRegister.encodePassword(repeatPassword)).role(user.getRole()).build();

            userUserRepository.save(manager);


            return "redirect:/login";

        }


        if (!password.equals(repeatPassword)) {
            model.addAttribute("errorPassword", true);
            return "security/register";
        }
        model.addAttribute("registerSuccess", true);
        userService.registerNewUser(user);
        return "users/guest/index";
    }*/


}
