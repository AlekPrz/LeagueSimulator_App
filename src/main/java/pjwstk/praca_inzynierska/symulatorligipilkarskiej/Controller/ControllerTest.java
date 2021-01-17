package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

@Controller
@RequiredArgsConstructor

public class ControllerTest {
    private final UserRepository<User> userUserRepository;
    private final ContractRepository contractRepository;
    private final ManagerTeamRepository managerTeamRepository;
    private final MatchTeamRepository matchTeamRepository;
    private final UserRepository<Player> playerUserRepository;
    private final SeasonTeamRepository seasonTeamRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;

    @GetMapping("/usuwanie")
    public String usunWszystkieDane(Model model) {

        if (!userUserRepository.findAll().isEmpty()) {
            model.addAttribute("removeAll", true);
        }

        return "deletePage";
    }

    @PostMapping("/usuwaniePost")
    public String usunWszystkieDanePost() {

        contractRepository.deleteAll();
        managerTeamRepository.deleteAll();

        for (MatchTeam tmp : matchTeamRepository.findAll()) {

            for (Player tmp1 : tmp.getHomeTeamPlayers()) {
                tmp1.getMatchTeamsHome().remove(tmp);
                playerUserRepository.save(tmp1);
            }
            for (Player tmp1 : tmp.getVisitTeamPlayers()) {
                tmp1.getMatchTeamsVisit().remove(tmp);
                playerUserRepository.save(tmp1);
            }

            tmp.getHomeTeamPlayers().clear();
            tmp.getVisitTeamPlayers().clear();


            matchTeamRepository.save(tmp);

        }
        matchTeamRepository.deleteAll();
        seasonTeamRepository.deleteAll();
        teamRepository.deleteAll();
        seasonRepository.deleteAll();
        playerUserRepository.deleteAll();


        return "deletePage";

    }
    @PostMapping("/dodawaniePost")
    public String dodajDanePost() {

      //dodać admina
      // dodać 2 managerów
      // dodać 3 drużyny z managerami
      // dodać ze 2/3 graczy
      // dodać wybieranie składu
      // dodać linki
      // dodać messsageRepository

        if (userUserRepository.findUserByUsername("admin").isEmpty()) {
            userUserRepository.save
                    (User.builder().username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .repeatPassword(passwordEncoder.encode("admin"))
                            .role(Role.ADMIN).build());
        }
        if (userUserRepository.findUserByUsername("manager1").isEmpty()) {
            userUserRepository.save
                    (User.builder().username("manager1")
                            .password(passwordEncoder.encode("manager1"))
                            .repeatPassword(passwordEncoder.encode("manager1"))
                            .role(Role.MANAGER).build());
        }

        if (userUserRepository.findUserByUsername("manager2").isEmpty()) {
            userUserRepository.save
                    (User.builder().username("manager2")
                            .password(passwordEncoder.encode("manager2"))
                            .repeatPassword(passwordEncoder.encode("manager2"))
                            .role(Role.MANAGER).build());

        }

        return "redirect:/usuwanie";


    }

    }


