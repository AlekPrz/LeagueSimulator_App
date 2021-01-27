package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

import java.time.LocalDate;

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
    private final MessageRepository messageRepository;

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
        messageRepository.deleteAll();


        return "deletePage";

    }

    @PostMapping("/dodawaniePost")
    public String dodajDanePost() {


        if (userUserRepository.findUserByUsername("admin").isEmpty()) {
            userUserRepository.save
                    (User.builder().username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .repeatPassword(passwordEncoder.encode("admin"))
                            .role(Role.ADMIN).build());
        }

        if (teamRepository.findByShortName("JED").isEmpty() && userUserRepository.findUserByUsername("manager1").isEmpty()
        && userUserRepository.findUserByUsername("gracz1").isEmpty() && userUserRepository.findUserByUsername("gracz2").isEmpty()) {

            Team team = Team.builder().name("Team Jeden").shortName("JED").colors("Czerwone").build();

            Manager manager = Manager.builder().username("manager1")
                    .password(passwordEncoder.encode("manager1"))
                    .repeatPassword(passwordEncoder.encode("manager1"))
                    .role(Role.MANAGER).build();

            userUserRepository.save(manager);
            teamRepository.save(team);

            ManagerTeam managerTeam1 = ManagerTeam.builder()
                    .isCurrently(true)
                    .startOfContract(LocalDate.now())
                    .team(team)
                    .manager(manager)
                    .build();

            managerTeamRepository.save(managerTeam1);

            Player player1 = Player.builder().name("Alek").surname("Przybysz").role(Role.PLAYER)
                    .password(passwordEncoder.encode("gracz1")).repeatPassword(passwordEncoder.encode("gracz1"))
                    .username("gracz1").shirtName("gracz1").age(25).position(Position.OBRONCA).build();


            Player player2 = Player.builder().name("Piotr").surname("Przybysz").role(Role.PLAYER)
                    .password(passwordEncoder.encode("gracz2")).repeatPassword(passwordEncoder.encode("gracz2"))
                    .username("gracz2").shirtName("gracz2").age(32).position(Position.NAPASTNIK).build();

            Contract contract1 = Contract.builder()
                    .startOfContract(LocalDate.now())
                    .player(player1)
                    .team(team)
                    .goals(0L)
                    .isCurrently(true)
                    .build();

            Contract contract2 = Contract.builder()
                    .startOfContract(LocalDate.now())
                    .player(player2)
                    .team(team)
                    .goals(0L)
                    .isCurrently(true)
                    .build();

            userUserRepository.save(player1);
            userUserRepository.save(player2);
            contractRepository.save(contract1);
            contractRepository.save(contract2);






        }
        if (teamRepository.findByShortName("DWA").isEmpty() && userUserRepository.findUserByUsername("manager2").isEmpty()
                && userUserRepository.findUserByUsername("gracz3").isEmpty() && userUserRepository.findUserByUsername("gracz4").isEmpty()) {


            Team team = Team.builder().name("Team Dwa").shortName("DWA").colors("Niebieskie").build();

            Manager manager = Manager.builder().username("manager2")
                    .password(passwordEncoder.encode("manager2"))
                    .repeatPassword(passwordEncoder.encode("manager2"))
                    .role(Role.MANAGER).build();

            userUserRepository.save(manager);
            teamRepository.save(team);

            ManagerTeam managerTeam1 = ManagerTeam.builder()
                    .isCurrently(true)
                    .startOfContract(LocalDate.now())
                    .team(team)
                    .manager(manager)
                    .build();


            managerTeamRepository.save(managerTeam1);

            Player player1 = Player.builder().name("Krystian").surname("Wilczak").role(Role.PLAYER)
                    .password(passwordEncoder.encode("gracz3")).repeatPassword(passwordEncoder.encode("gracz3"))
                    .username("gracz3").shirtName("gracz3").age(25).position(Position.OBRONCA).build();


            Player player2 = Player.builder().name("Zenon").surname("Kowalwski").role(Role.PLAYER)
                    .password(passwordEncoder.encode("gracz4")).repeatPassword(passwordEncoder.encode("gracz4"))
                    .username("gracz4").shirtName("gracz4").age(20).position(Position.POMOCNIK).build();

            Contract contract1 = Contract.builder()
                    .startOfContract(LocalDate.now())
                    .player(player1)
                    .team(team)
                    .goals(0L)
                    .isCurrently(true)
                    .build();

            Contract contract2 = Contract.builder()
                    .startOfContract(LocalDate.now())
                    .player(player2)
                    .team(team)
                    .goals(0L)
                    .isCurrently(true)
                    .build();

            userUserRepository.save(player1);
            userUserRepository.save(player2);
            contractRepository.save(contract1);
            contractRepository.save(contract2);

        }
        if (teamRepository.findByShortName("TRZY").isEmpty()
                && userUserRepository.findUserByUsername("manager3").isEmpty()
                && userUserRepository.findUserByUsername("gracz5").isEmpty()
                && userUserRepository.findUserByUsername("gracz6").isEmpty()) {

            Team team = Team.builder().name("Team Trzy").shortName("TRZY").colors("Białe").build();

            Manager manager = Manager.builder().username("manager3")
                    .password(passwordEncoder.encode("manager3"))
                    .repeatPassword(passwordEncoder.encode("manager3"))
                    .role(Role.MANAGER).build();

            userUserRepository.save(manager);
            teamRepository.save(team);

            ManagerTeam managerTeam1 = ManagerTeam.builder()
                    .isCurrently(true)
                    .startOfContract(LocalDate.now())
                    .team(team)
                    .manager(manager)
                    .build();


            managerTeamRepository.save(managerTeam1);


            Player player1 = Player.builder().name("Krystian").surname("Wilczak").role(Role.PLAYER)
                    .password(passwordEncoder.encode("gracz5")).repeatPassword(passwordEncoder.encode("gracz5"))
                    .username("gracz5").age(40).shirtName("gracz5").position(Position.NAPASTNIK).build();


            Player player2 = Player.builder().name("Zenon").surname("Kowalwski").role(Role.PLAYER)
                    .password(passwordEncoder.encode("gracz6")).repeatPassword(passwordEncoder.encode("gracz6"))
                    .username("gracz6").age(18).shirtName("gracz6").position(Position.BRAMKARZ).build();

            Contract contract1 = Contract.builder()
                    .startOfContract(LocalDate.now())
                    .player(player1)
                    .team(team)
                    .goals(0L)
                    .isCurrently(true)
                    .build();

            Contract contract2 = Contract.builder()
                    .startOfContract(LocalDate.now())
                    .player(player2)
                    .team(team)
                    .goals(0L)
                    .isCurrently(true)
                    .build();

            userUserRepository.save(player1);
            userUserRepository.save(player2);
            contractRepository.save(contract1);
            contractRepository.save(contract2);

        }

        return "redirect:/usuwanie";


    }

}


