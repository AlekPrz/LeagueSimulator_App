package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Service.UserRegister;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor

public class AdminController {


    private final UserRegister userService;
    private final UserRepository<Manager> userRepository;
    private final TeamRepository teamRepository;



    @GetMapping("/")
    public String dash() {


        return "admin/dash";

    }

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("rolesAdmin", true);

        return "security/register";
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
            return "security/register";
        }

        userService.registerNewUser(user);
        return "redirect:/login";
    }


    @GetMapping("/panelDruzyn")
    public String addTeams(Model model) {


        model.addAttribute("team", teamRepository.findAll());
        return "admin/allTeamsForAdmin";

    }


    @GetMapping("/dodajDrużyne")
    public String addTeam(Model model) {
        model.addAttribute("team", new Team());
        return "admin/addTeam";

    }

    @PostMapping("/dodajDrużyne")
    public String addTeamPost(Team team) {

        teamRepository.save(team);
        return "redirect:/";

    }

}
