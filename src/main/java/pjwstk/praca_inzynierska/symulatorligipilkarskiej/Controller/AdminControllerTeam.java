package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminControllerTeam {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final ManagerService managerService;
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
        List<Manager> managers = managerService.findManagers();
        model.addAttribute("manager", managers);
        model.addAttribute("managerTeam", new ManagerTeam());
        model.addAttribute("team", new Team());
        return "admin/addTeam";
    }


    @PostMapping("/dodajDrużyne")
    public String addTeamPost(@Valid @ModelAttribute Team team, BindingResult bindingResult, ManagerTeam managerTeam, Model model) {


        Map<String, String> allErrorsFromMyValidate = new LinkedHashMap<>();


        allErrorsFromMyValidate.putAll(teamService.checkErrors(team, managerTeam, bindingResult));

        if (!allErrorsFromMyValidate.isEmpty()) {

            List<Manager> managers = managerService.findManagers();
            model.addAttribute("error", allErrorsFromMyValidate);
            model.addAttribute("manager", managers);
            model.addAttribute("managerTeam", managerTeam);
            model.addAttribute("team", team);
            return "admin/addTeam";
        }

        teamService.createTeam(team, managerTeam);
        return "redirect:/admin/panelDruzyn";

    }

    @GetMapping("/druzyna/gracze/{id}")
    public String getEmployeesById(@PathVariable Long id, Model model) {


        model.addAttribute("players", teamService.getAllPlayersInThatTeam(id));

        return "admin/playersInThatTeam";
    }


}
