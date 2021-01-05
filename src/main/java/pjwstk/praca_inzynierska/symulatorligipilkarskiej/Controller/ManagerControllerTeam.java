package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.PasswordGenerator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerControllerTeam {

    private final TeamRepository teamRepository;
    private final PlayerService playerService;
    private final UserRepository<Player> playerUserRepository;
    private final ContractRepository contractRepository;
    private final TeamService teamService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/dodajGracza/{id}")
    public String addPlayerGet(@PathVariable Long id, Model model) {


        model.addAttribute("player", new Player());


        model.addAttribute("teamId", teamRepository.findById(id).orElse(null).getId());


        model.addAttribute("contract", new Contract());
        model.addAttribute("position", Position.values());


        return "manager/myTeam/addPlayer";
    }

    @PostMapping("/dodajPiłkarza")
    public String addPlayerPost(@Valid @ModelAttribute Player player, BindingResult bindingResult, Contract contract, Model model, Long teamId) {

        Map<String, String> allErrorsFromMyValidate = new LinkedHashMap<>();


        allErrorsFromMyValidate.putAll(playerService.checkErrors(player, contract, bindingResult));


        if (!allErrorsFromMyValidate.isEmpty()) {

            model.addAttribute("errors", allErrorsFromMyValidate);
            model.addAttribute("player", player);
            model.addAttribute("contract", contract);
            model.addAttribute("teamId", teamId);
            model.addAttribute("position", Position.values());
            return "manager/myTeam/addPlayer";
        }

        contract.setTeam(teamRepository.findById(teamId).orElse(null));


        String resultPassword = PasswordGenerator.stringGenerator();
        player.setPassword(passwordEncoder.encode(resultPassword));
        player.setRepeatPassword(passwordEncoder.encode(resultPassword));

        playerService.createPlayer(player, contract);


        model.addAttribute("registerSuccess",true);
        model.addAttribute("haslo", resultPassword);
        model.addAttribute("login", player.getUsername());


        return "manager/myTeam/InfoForPlayer";
    }

    @GetMapping("/podejrzyjZawodników/{id}")
    public String getAllPlayersInThatTeam(@PathVariable Long id, Model model) {


        model.addAttribute("players", teamService.getAllPlayersInThatTeam(id));

        return "manager/myTeam/playersInThatTeam";
    }
}
