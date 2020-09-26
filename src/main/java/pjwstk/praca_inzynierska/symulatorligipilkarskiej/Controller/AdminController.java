package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.UserRegister;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor

public class AdminController {


    private final UserRegister userService;
    private final UserRepository<Manager> userRepository;
    private final TeamService teamService;
    private final ManagerTeamRepository managerTeamRepository;
    private final UserRepository<Player> playerUserRepository;


    @GetMapping("/")
    public String dash() {


        return "admin/dash";

    }

    @GetMapping("/dodajUzytkownika")
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


        if (user.getRole().getDescription().equals("ROLE_MANAGER")) {


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
    public String getTeams(Model model) {


        model.addAttribute("team", teamService.getAllTeam());
        return "admin/allTeamsForAdmin";

    }

    @GetMapping("/panelGraczy")
    public String getPlayers(Model model) {
        List<Player> players = new ArrayList<>();

        for (User tmp : playerUserRepository.findAll()) {
            if (tmp instanceof Player) {
                players.add((Player) tmp);
            }
        }


        model.addAttribute("player", players);
        model.addAttribute("position", Position.values());

        return "admin/allPlayersForAdmin";

    }


    @GetMapping("/dodajPiłkarza")
    public String addPlayerGet(Model model) {


        model.addAttribute("player", new Player());

        model.addAttribute("position", Position.values());


        return "admin/addPlayer";

    }

    @PostMapping("/dodajPiłkarza")
    public String addPlayerPost(Player player1) {

        System.out.println(player1);
        playerUserRepository.save(player1);


        return "redirect:/";


    }


    @GetMapping("/dodajDrużyne")
    public String addTeam(Model model) {

        List<Manager> managers = new ArrayList<>();

        for (User tmp : userRepository.findAll()) {
            if (tmp instanceof Manager) {
                managers.add((Manager) tmp);
            }
        }


        model.addAttribute("manager", managers);
        model.addAttribute("managerTeam", new ManagerTeam());
        model.addAttribute("team", new Team());
        return "admin/addTeam";

    }

    @PostMapping("/dodajDrużyne")
    public String addTeamPost(Team team, ManagerTeam managerTeam) {


        teamService.createTeam(team);

        Manager manager = managerTeam.getManager();


        ManagerTeam managerTeam1 = ManagerTeam.builder()
                .isCurrently(true)
                .endOfContract(managerTeam.getEndOfContract())
                .startOfContract(managerTeam.getStartOfContract())
                .team(team)
                .manager(manager)
                .build();


        managerTeamRepository.save(managerTeam1);

        managerTeam.getManager().getManagerTeams().add(managerTeam);
        team.getManagerTeams().add(managerTeam);


        return "redirect:/";

    }

}
