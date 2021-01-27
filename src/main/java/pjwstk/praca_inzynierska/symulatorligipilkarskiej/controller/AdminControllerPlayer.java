package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Position;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.PasswordGenerator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;
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
    private final TeamService teamService;
    private final PlayerService playerService;
    private final ContractRepository contractRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository<User> userRepository;


    @GetMapping("/")
    public String dash() {
        return "admin/dash";
    }

    @GetMapping("/dodajUzytkownika")
    public String registerGet(Model model) {

        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));
        roles.removeIf(p -> p.equals(Role.PLAYER));
        roles.removeIf(p -> p.equals(Role.FAN));



        model.addAttribute("user", new User());
        model.addAttribute("roles",roles);
        model.addAttribute("rolesAdmin", true);
        return "security/register";
    }


    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user, Model model) {

        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();

        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));
        roles.removeIf(p -> p.equals(Role.PLAYER));
        roles.removeIf(p -> p.equals(Role.FAN));


        if (!password.equals(repeatPassword)) {
            model.addAttribute("user",user);
            model.addAttribute("errorPassword", true);
            model.addAttribute("roles",roles);
            model.addAttribute("rolesAdmin", true);
            return "security/register";
        }

        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            model.addAttribute("user",user);
            model.addAttribute("errorUsername", true);
            model.addAttribute("roles",roles);
            model.addAttribute("rolesAdmin", true);
            return "security/register";
        }
        model.addAttribute("registerSuccess",true);
        userService.registerNewUser(user);
        return "security/login";
    }


    @GetMapping("/panelGraczy")
    public String getPlayers(Model model) {


        model.addAttribute("player", playerService.getAllPlayers());
        model.addAttribute("position", Position.values());
        return "admin/players/allPlayersForAdmin";

    }


    @GetMapping("/dodajPilkarza")
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

        String resultPassword = PasswordGenerator.stringGenerator();
        player.setPassword(passwordEncoder.encode(resultPassword));
        player.setRepeatPassword(passwordEncoder.encode(resultPassword));

        playerService.createPlayer(player, contract);

        model.addAttribute("registerSuccess",true);
        model.addAttribute("haslo", resultPassword);
        model.addAttribute("login", player.getUsername());


        return "admin/players/InfoForPlayer";
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

