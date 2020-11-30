package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.PlayerToFormDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.PlayersIdsFromFormData;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.mapper.Mappers;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.mapper.PlayersDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ContractService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ContractService contractService;
    private final MatchTeamRepository matchTeamRepository;
    private final TeamService teamService;

    // private final TeamRepository teamRepository;
    // private final UserRepository<Manager> managerUserRepository;
    // private final TeamService teamService;
    // private final MatchTeamRepository matchTeamRepository;

    @GetMapping("/")
    public String dash() {
        return "manager/dashboard";
    }

    @GetMapping("/mojaDruzyna")
    public String getMyTeam(Model model) {

        Manager manager = managerService.getCurrentManager();


        ManagerTeam managerTeam =
                manager.getManagerTeams()
                        .stream()
                        .filter(p -> p.getIsCurrently().equals(true))
                        .findFirst()
                        .orElse(null);


        model.addAttribute("manager", managerTeam);


        return "manager/myTeam";

    }

    @GetMapping("/terminarzDruzyny")
    public String getSchedule(Model model) {


        List<MatchTeam> getMyTeamMatches = managerService.getCurrentMatches();


        model.addAttribute("matchTeam", getMyTeamMatches);

        return "manager/schedule";

    }

    @GetMapping("/terminarzDruzyny/ustalSklad/{id}")
    public String chooseSquadOnMatch(@PathVariable Long id, Model model) {


        List<Player> players = teamService
                .getAllPlayersInThatTeam(managerService.getCurrentPlayersOfTeam().getId());


        model.addAttribute("form", new PlayersDto(players));



/*
        var players = contractService
                .getAllPlayersIdsFromTeam(managerService.getCurrentPlayersOfTeam().getId())
                .stream()
                .map(Mappers::fromPlayerToPlayerToFormDto)
                .collect(Collectors.toList());
        var ids = players.stream().map(PlayerToFormDto::getId).collect(Collectors.toList());


        model.addAttribute("players", players);
        model.addAttribute("playersFromForm", ids);*/

        return "manager/scheduleInsertSquad";
    }

    @PostMapping("/terminarzDruzyny/ustalSklad")
    public String chooseSquadOnMatchPost(@ModelAttribute PlayersDto playersIdsFromFormData) {
        System.out.println(playersIdsFromFormData);
        return "redirect:/manager/terminarzDruzyny";
    }


}
