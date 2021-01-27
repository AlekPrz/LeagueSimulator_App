package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final MatchTeamRepository matchTeamRepository;
    private final TeamService teamService;

    private final SeasonTeamRepository seasonTeamRepository;




    @GetMapping("/druzyny")
    public String getTeamsForGuest(Model model) {


        if (teamRepository.findAll().isEmpty()) {
            model.addAttribute("error", true);

        }


        model.addAttribute("team", teamRepository.findAll());
        return "users/guest/allTeamForGuest";
    }
    @GetMapping("/druzyny/gracze/{id}")
    public String getEmployeesById(@PathVariable Long id, Model model) {


        model.addAttribute("players", teamService.getAllPlayersInThatTeam(id));

        return "/users/guest/playersInThatTeam";
    }



    @GetMapping("/terminarz")
    public String getSchedule(@PageableDefault(size = 2) Pageable pageable, Model model) {

        Page<MatchTeam> page = matchTeamRepository.findAllByOrderByQueue(pageable);


        if (matchTeamRepository.findAll().isEmpty()) {
            model.addAttribute("error", true);
        }


        model.addAttribute("page", page);

        return "users/guest/scheduleForGuest";

    }


    @GetMapping("/tabela")
    public String getTeamsTableForGuest(Model model) {

        if (seasonTeamRepository.findAll().isEmpty()) {
            model.addAttribute("error", true);
        }


        model.addAttribute("teamTable", seasonTeamRepository.findAllByOrderByPointsDescGoalsDesc());
        return "users/guest/TableForGuest";
    }


}



