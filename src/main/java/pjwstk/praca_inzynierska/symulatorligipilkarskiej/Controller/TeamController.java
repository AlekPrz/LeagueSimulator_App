package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Schedule;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.security2.UserDetailsServiceImpl;

@Controller
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    @GetMapping("/drużyny")
    public String getTeamsForGuest(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            String username = ((User) principal).getUsername();
            System.out.println("Name " + username);
        }
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            System.out.println("JESTEM INSTANCJA TEGO:" + username);
        }


        model.addAttribute("team", teamRepository.findAll());
        return "users/guest/allTeamForGuest";
    }

    @GetMapping("/terminarz")
    public String getScheduleForUser(Model model) {

        Schedule schedule = null;

        model.addAttribute("error", true);


        return "users/guest/scheduleForGuest";
    }
}



