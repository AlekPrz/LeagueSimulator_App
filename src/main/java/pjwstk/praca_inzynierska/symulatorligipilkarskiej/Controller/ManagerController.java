package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.security2.UserDetailsServiceImpl;


@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final TeamRepository teamRepository;


    public ManagerController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    @GetMapping("dashboard")
    public String dash() {
        return "manager/dashboard";
    }
/*
    @GetMapping("myTeam")
    public String getMyTeam(Model model) {


*/
/*
            model.addAttribute("team",teamRepository.findAll())
*//*

    }
*/




}
