package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;
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

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor

public class AdminControllerPlayer {


    private final UserRegister userService;
    private final UserRepository<Manager> userRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final ContractRepository contractRepository;


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

    //Poprawic

    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user, Model model) {

        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();


        if (!password.equals(repeatPassword)) {
            model.addAttribute("errorPassword", true);
            return "security/register";
        }

        userService.registerNewManager(user);
        return "redirect:/login";
    }


    @GetMapping("/panelGraczy")
    public String getPlayers(Model model) {


        model.addAttribute("player", playerService.getAllPlayers());
        model.addAttribute("position", Position.values());
        return "admin/players/allPlayersForAdmin";

    }


    @GetMapping("/dodajPiłkarza")
    public String addPlayerGet(Model model) {


        model.addAttribute("player", new Player());
        model.addAttribute("team", teamService.getAllTeam());
        model.addAttribute("contract", new Contract());
        model.addAttribute("position", Position.values());


        return "admin/players/addPlayer";

    }

    @PostMapping("/dodajPiłkarza")
    public String addPlayerPost(@Valid @ModelAttribute Player player, BindingResult bindingResult, Contract contract, Model model) {

        Map<String, String> allErrorsFromMyValidate = new LinkedHashMap<>();


        allErrorsFromMyValidate.putAll(playerService.checkErrors(player, contract, bindingResult));


        if (!allErrorsFromMyValidate.isEmpty()) {

            model.addAttribute("errors", allErrorsFromMyValidate);
            model.addAttribute("player", player);
            model.addAttribute("contract", contract);
            model.addAttribute("team", teamService.getAllTeam());
            model.addAttribute("position", Position.values());
            return "admin/players/addPlayer";
        }

        playerService.createPlayer(player, contract);
        return "redirect:/admin/panelGraczy";
    }


    @GetMapping("/edytujGracza/{id}")
    public String workerModifyGet(Model model, @PathVariable Long id) {
        model.addAttribute("player", playerService.findPlayerById(id));
        model.addAttribute("team", teamService.getAllTeam());
        model.addAttribute("position", Position.values());

        if (playerService.findCurrentlyContract(id) != null) {
            model.addAttribute("contract", playerService.findCurrentlyContract(id));
        } else {
            model.addAttribute("contract", new Contract());

        }

        return "admin/players/modifyPlayer";
    }


    @PostMapping("/edytujGracza")
    public String workerModifyPost(@Valid @ModelAttribute Player player, BindingResult bindingResult,
                                   Contract contract, Model model) {

        Map<String, String> allErrorsFromMyValidate = new LinkedHashMap<>();

        allErrorsFromMyValidate.putAll(playerService.checkErrorsModify(player, contract, bindingResult));


        if (!allErrorsFromMyValidate.isEmpty()) {

            model.addAttribute("errors", allErrorsFromMyValidate);
            model.addAttribute("player", player);
            model.addAttribute("contract", contract);
            model.addAttribute("team", teamService.getAllTeam());
            model.addAttribute("position", Position.values());
            return "admin/players/modifyPlayer";
        }


        playerService.modifyPlayer(player, contract);
        return "redirect:/admin/panelGraczy";
    }


    @PostMapping("/usunPilkarza")
    public String deletePlayer(Long id) {

        for (Contract contract : contractRepository.findAll()) {
            if (contract.getPlayer().getId().equals(id)) {
                contractRepository.delete(contract);
            }
        }

        playerService.deletePlayer(id);
        return "redirect:/admin/panelGraczy";
    }


}

