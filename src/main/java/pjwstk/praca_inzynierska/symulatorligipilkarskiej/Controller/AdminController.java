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
@RequestMapping("/admin")

public class AdminController  {


    @Autowired
    private UserRegister userService;
    @Autowired
    private UserRepository<Manager> userRepository;

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user, Model model) {
        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();

        System.out.println(user.getRole().getDescription());


        if (user.getRole().getDescription().equals("ROLE_MANAGER")) {

            System.out.println("no jestem u");

            Manager manager = Manager.builder().username(user.getUsername()).password(UserRegister.encodePassword(password))
                    .repeatPassword(UserRegister.encodePassword(repeatPassword)).role(user.getRole()).build();

            userRepository.save(manager);


            return "redirect:/login";

        }


        if (!password.equals(repeatPassword)) {
            model.addAttribute("errorPassword", true);
            return "users/register";
        }

        userService.registerNewUser(user);
        return "redirect:/login";
    }


}
