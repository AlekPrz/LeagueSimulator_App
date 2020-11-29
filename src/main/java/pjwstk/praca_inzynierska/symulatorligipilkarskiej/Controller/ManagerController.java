package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.PlayersDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ContractService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import java.util.*;


@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final TeamRepository teamRepository;
    private final UserRepository<Manager> managerUserRepository;
    private final ManagerService managerService;
    private final TeamService teamService;
    private final MatchTeamRepository matchTeamRepository;


    public ManagerController(TeamRepository teamRepository,
                             UserRepository<Manager> managerUserRepository,
                             ManagerService managerService, TeamService teamService, MatchTeamRepository matchTeamRepository

    ) {
        this.teamRepository = teamRepository;
        this.managerUserRepository = managerUserRepository;
        this.managerService = managerService;
        this.teamService = teamService;
        this.matchTeamRepository = matchTeamRepository;

    }


    @GetMapping("/")
    public String dash() {
        return "manager/dashboard";
    }

    @GetMapping("/mojaDruzyna")
    public String getMyTeam(Model model) {


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Manager> manager = Optional.empty();

        if (principal instanceof UserDetails) {

            String usernameName = ((UserDetails) principal).getUsername();
            manager = managerUserRepository.findManagerByUsername(usernameName);

        }

        System.out.println(manager.get().getId());

        if (manager.isPresent()) {

            ManagerTeam managerTeam =
                    manager.get()
                            .getManagerTeams()
                            .stream()
                            .filter(p -> p.getIsCurrently().equals(true))
                            .findFirst()
                            .orElse(null);


            model.addAttribute("manager", managerTeam);
        } else {
            throw new RuntimeException("Manager problem with team");
        }


        return "manager/myTeam";

    }

    @GetMapping("/terminarzDruzyny")
    public String getSchedule(Model model) {


        List<MatchTeam> getMyTeamMatches = new ArrayList<>();

        Manager manager = managerService.getCurrentManager();

        Team team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam).orElse(null);


        getMyTeamMatches.addAll(team.getHomeGames());
        getMyTeamMatches.addAll(team.getVisitGames());


        getMyTeamMatches.sort(Comparator.comparing(MatchTeam::getQueue));


        if (manager != null) {
            model.addAttribute("manager", true);
        }

        System.out.println(manager);

        model.addAttribute("matchTeam", getMyTeamMatches);

        return "manager/schedule";

    }

    @GetMapping("/terminarzDruzyny/ustalSklad/{id}")
    public String getInsert(@PathVariable Long id, Model model) {

        Manager manager = managerService.getCurrentManager();

        Team team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam).orElse(null);


        List<Player> players = teamService.getAllPlayersInThatTeam(team.getId());


        model.addAttribute("matchTeam", matchTeamRepository.findById(id));
        model.addAttribute("player", new PlayersDto(players));

        return "manager/scheduleInsertSquad";

    }

    @PostMapping("/terminarzDruzyny/ustalSklad")
    public String postPlayer(@ModelAttribute PlayersDto player) {


        System.out.println(player.getPlayerList());

        return "redirect:/manager/terminarzDruzyny";

    }


}
