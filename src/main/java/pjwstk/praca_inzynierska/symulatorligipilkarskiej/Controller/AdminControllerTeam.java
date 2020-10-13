package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;



@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminControllerTeam {

    private final TeamService teamService;
    private final ManagerTeamRepository managerTeamRepository;


    @GetMapping("/panelDruzyn")
    public String getTeams(Model model, @RequestParam(required = false) String keyword) {
        if (keyword != null) {
            model.addAttribute("team", teamService.findByKeyword(keyword));
        } else {
            model.addAttribute("team", teamService.getAllTeam());
        }
        return "admin/allTeamsForAdmin";
    }

    @PostMapping("/usunDruzyne")
    public String deleteTeam(Long id) {
        teamService.deleteTeam(id);
        return "redirect:/admin/panelDruzyn";
    }

    @GetMapping("/dodajDrużyne")
    public String addTeam(Model model) {
        teamService.addTeamView(model);
        return "admin/addTeam";
    }


    @PostMapping("/dodajDrużyne")
    public String addTeamPost(Team team, ManagerTeam managerTeam) {


        teamService.createTeam(team);



        //Dopisac walidacje która sprawdza czy trener ma drużyne
        Manager manager = managerTeam.getManager();


        ManagerTeam managerTeam1 = ManagerTeam.builder()
                .isCurrently(true)
                .endOfContract(managerTeam.getEndOfContract())
                .startOfContract(managerTeam.getStartOfContract())
                .team(team)
                .manager(manager)
                .build();


        managerTeamRepository.save(managerTeam1);


        manager.getManagerTeams().add(managerTeam);
        team.getManagerTeams().add(managerTeam);


        return "redirect:/admin/panelDruzyn";

    }


}
