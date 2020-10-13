package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.UserRegister;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor

public class AdminControllerPlayer {


    private final UserRegister userService;
    private final UserRepository<Manager> userRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final ContractRepository contractRepository;
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
        model.addAttribute("team", teamService.getAllTeam());
        model.addAttribute("contract", new Contract());
        model.addAttribute("position", Position.values());
        return "admin/addPlayer";

    }

    @PostMapping("/dodajPiłkarza")
    public String addPlayerPost(Player player, Contract contract) {

        playerService.createPlayer(player,contract);
        return "redirect:/admin/panelGraczy";

    }

    @PostMapping("/usunPilkarza")
    public String deletePlayer(Long id) {

        playerService.deletePlayer(id);
        return "redirect:/admin/panelGraczy";
    }





}
