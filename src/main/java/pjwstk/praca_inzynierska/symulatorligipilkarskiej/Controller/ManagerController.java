package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.security2.UserDetailsServiceImpl;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final TeamRepository teamRepository;
    private final UserRepository<Manager> managerUserRepository;


    public ManagerController(TeamRepository teamRepository, UserRepository<Manager> managerUserRepository) {
        this.teamRepository = teamRepository;
        this.managerUserRepository = managerUserRepository;
    }


    @GetMapping("/")
    public String dash() {
        return "manager/dashboard";
    }

    @GetMapping("mojaDruzyna")
    public String getMyTeam(Model model) {


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Manager> manager = Optional.empty();

        if (principal instanceof UserDetails) {

            String usernameName = ((UserDetails) principal).getUsername();
            manager = managerUserRepository.findManagerByUsername(usernameName);

        }

        System.out.println(manager.get().getId());

        if(manager.isPresent()) {

            ManagerTeam managerTeam =
                    manager.get()
                            .getManagerTeams()
                            .stream()
                            .filter(p -> p.getIsCurrently().equals(true))
                            .findFirst()
                            .orElse(null);



            model.addAttribute("manager", managerTeam);
        }
        else{
            throw new RuntimeException("Manager problem with team");
        }


        return "manager/myTeam";

    }


}
