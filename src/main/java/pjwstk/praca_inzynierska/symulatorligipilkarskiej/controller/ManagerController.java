package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.dto.mapper.PlayersDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.Counter;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MessageRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ContractService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import java.util.*;


@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ContractService contractService;
    private final MatchTeamRepository matchTeamRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MessageRepository messageRepository;

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


        if (managerTeam != null) {
            model.addAttribute("manager", managerTeam);
        } else {
            model.addAttribute("notInit", true);
        }


        return "manager/myTeam";

    }

    @GetMapping("/terminarzDruzyny")
    public String getSchedule(Model model) {


        List<MatchTeam> getMyTeamMatches = managerService.getCurrentMatches();


        List<Team> teams = managerService.getCurrentEnemy();


        Long idOfTeam;

        if (managerService.getCurrentPlayersOfTeam().isPresent()) {
            idOfTeam = managerService.getCurrentPlayersOfTeam().get().getId();
        } else {
            idOfTeam = null;
        }


        List<Player> players = teamService
                .getAllPlayersInThatTeam(idOfTeam);


        if (getMyTeamMatches != null && !getMyTeamMatches.isEmpty()) {

            model.addAttribute("currentlyTeam",managerService.getCurrentManagerTeam());
            model.addAttribute("matchTeam", getMyTeamMatches);
            model.addAttribute("players", players);
            model.addAttribute("sum", new Counter());
            model.addAttribute("managerUsername", managerService.getCurrentManager().getUsername());
        } else {
            model.addAttribute("matchTeamNotInit", true);
        }


        return "manager/schedule";

    }


    @GetMapping("/terminarzDruzyny/ustalSklad/{id}")
    public String chooseSquadOnMatch(@PathVariable Long id, Model model) {


        List<Player> players = teamService
                .getAllPlayersInThatTeam(managerService.getCurrentPlayersOfTeam().get().getId());

        List<Player> playersReadyOnMatch = new ArrayList<>();

        MatchTeam matchTeam = matchTeamRepository.findById(id).orElse(null);


        if (!Collections.disjoint(players, matchTeam.getHomeTeamPlayers())) {
            playersReadyOnMatch.addAll(matchTeam.getHomeTeamPlayers());
            model.addAttribute("homeTeamPlayersToDelete", true);
        }
        if (!Collections.disjoint(players, matchTeam.getVisitTeamPlayers())) {
            playersReadyOnMatch.addAll(matchTeam.getVisitTeamPlayers());
            model.addAttribute("visitTeamPlayersToDelete", true);
        }

        if (!playersReadyOnMatch.isEmpty()) {
            model.addAttribute("thereArePlayers", true);
            model.addAttribute("playersReadyOnMatch", playersReadyOnMatch);
        }


        model.addAttribute("form", new PlayersDto(players));
        model.addAttribute("matchTeam", matchTeam);


        return "manager/scheduleInsertSquad";
    }

    @PostMapping("/terminarzDruzyny/ustalSklad")
    public String chooseSquadOnMatchPost(@ModelAttribute PlayersDto playersIdsFromFormData, MatchTeam matchTeam) {


        System.out.println(playerService.getAllPlayers());

        List<Player> playersFromDb = new ArrayList<>();

        for (Player tmp : playersIdsFromFormData.getPlayerList()) {
            for (Player tmp1 : playerService.getAllPlayers()) {
                if (tmp.getId() == tmp1.getId()) {
                    playersFromDb.add(tmp1);
                }
            }
        }

        System.out.println(playersFromDb);


        MatchTeam matchTeam1 = matchTeamRepository.findById(matchTeam.getId()).orElse(null);


        for (Player player : playersFromDb) {
            if (matchTeam1.getHomeTeam().equals(managerService.getCurrentManagerTeam())) {
                player.getMatchTeamsHome().add(matchTeam1);
                matchTeam1.getHomeTeamPlayers().add(player);
            }
            if (matchTeam1.getVisitTeam().equals(managerService.getCurrentManagerTeam())) {
                player.getMatchTeamsVisit().add(matchTeam1);
                matchTeam1.getVisitTeamPlayers().add(player);

            }
            matchTeamRepository.save(matchTeam1);

        }


        return "redirect:/manager/terminarzDruzyny";
    }

    @PostMapping("/terminarzDruzyny/ustalSklad/usunSkladHome")
    public String deletePlayerMatch(Long homeTeamPlayersToDeleteId) {

        System.out.println(homeTeamPlayersToDeleteId);


        MatchTeam matchTeam1 = matchTeamRepository.findById(homeTeamPlayersToDeleteId).orElse(null);




        List<Player> toRemove = new ArrayList<>();

        for (Player tmp : matchTeam1.getHomeTeamPlayers()) {
            tmp.getMatchTeamsHome().remove(matchTeam1);
            toRemove.add(tmp);
        }

        matchTeam1.getHomeTeamPlayers().removeAll(toRemove);
        matchTeamRepository.save(matchTeam1);


        return "redirect:/manager/terminarzDruzyny";

    }

    @PostMapping("/terminarzDruzyny/ustalSklad/usunSkladVisit")
    public String deletePlayerVisit(Long visitTeamPlayersToDelete) {



        MatchTeam matchTeam1 = matchTeamRepository.findById(visitTeamPlayersToDelete).orElse(null);




        List<Player> toRemove = new ArrayList<>();

        for (Player tmp : matchTeam1.getVisitTeamPlayers()) {
            tmp.getMatchTeamsVisit().remove(matchTeam1);
            toRemove.add(tmp);
        }

        matchTeam1.getVisitTeamPlayers().removeAll(toRemove);
        matchTeamRepository.save(matchTeam1);


        return "redirect:/manager/terminarzDruzyny";

    }


}
