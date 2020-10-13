package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminControllerTeam {

    private final TeamService teamService;
    private final ManagerTeamRepository managerTeamRepository;
    private final ManagerService managerService;


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

    @GetMapping("/dodajDruzyne")
    public String addTeam(Model model) {
        List<Manager> managers = managerService.findManagers();
        model.addAttribute("manager", managers);
        model.addAttribute("managerTeam", new ManagerTeam());
        model.addAttribute("team", new Team());
        return "admin/addTeam";
    }


    @PostMapping("/dodajDruzyne")
    public String addTeamPost(
             Team team,
            ManagerTeam managerTeam) {

        Manager manager = managerTeam.getManager();


/*        Map<String, String> errors
                = bindingResult
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (v1, v2) -> v1 + ", " + v2
                ));


        if(bindingResult.hasErrors()){
            model.addAttribute("errors",errors);
            return "admin/addTeam";
        }




        if (manager == null) {

            model.addAttribute("error", true);

            return "admin/addTeam";
        }*/




        ManagerTeam managerTeam1 = ManagerTeam.builder()
                .isCurrently(true)
                .endOfContract(managerTeam.getEndOfContract())
                .startOfContract(managerTeam.getStartOfContract())
                .team(team)
                .manager(manager)
                .build();

        teamService.createTeam(team);
        managerTeamRepository.save(managerTeam1);

        manager.getManagerTeams().add(managerTeam);
        team.getManagerTeams().add(managerTeam);


        return "redirect:/admin/panelGraczy";




    }


}
