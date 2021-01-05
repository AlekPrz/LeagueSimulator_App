package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;

@Controller
public class TeamController {

    private final TeamRepository teamRepository;
    private final MatchTeamRepository matchTeamRepository;


    private final SeasonTeamRepository seasonTeamRepository;


    public TeamController(TeamRepository teamRepository, MatchTeamRepository matchTeamRepository, SeasonTeamRepository seasonTeamRepository) {
        this.seasonTeamRepository = seasonTeamRepository;
        this.teamRepository = teamRepository;
        this.matchTeamRepository = matchTeamRepository;
    }


    @GetMapping("/drużyny")
    public String getTeamsForGuest(Model model) {
/*
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            String username = ((User) principal).getUsername();
            System.out.println("Name " + username);
        }
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            System.out.println("JESTEM INSTANCJA TEGO:" + username);
        }
*/


        if (teamRepository.findAll().isEmpty()) {
            model.addAttribute("error", true);

        }


        model.addAttribute("team", teamRepository.findAll());
        return "users/guest/allTeamForGuest";
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



