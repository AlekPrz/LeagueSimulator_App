package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Message;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository<Manager> managerUserRepository;
    private final UserRepository<User> userRepository;


    public List<Manager> findManagers() {

        List<Manager> managers = new ArrayList<>();

        for (User tmp : managerUserRepository.findAll()) {
            if (tmp instanceof Manager) {
                managers.add((Manager) tmp);
            }
        }

        return managers;

    }

    public Manager findManagerById(Long id) {

        return managerUserRepository.findById(id).orElse(null);

    }

    public Team getCurrentManagerTeam() {

        Manager manager = getCurrentManager();
        Team team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam).orElse(null);

        return team;
    }

    public Manager getCurrentManager() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Manager> manager = Optional.empty();

        if (principal instanceof UserDetails) {

            String usernameName = ((UserDetails) principal).getUsername();
            manager = managerUserRepository.findManagerByUsername(usernameName);

        }


        return manager.orElse(null);

    }
    public User getCurrentUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = Optional.empty();

        if (principal instanceof UserDetails) {

            String usernameName = ((UserDetails) principal).getUsername();
            user = userRepository.findUserByUsername(usernameName);

        }


        return user.orElse(null);

    }

    public Manager getCurrentEnemy(MatchTeam matchTeam) {


        for (ManagerTeam tmp : matchTeam.getHomeTeam().getManagerTeams()) {

            if (!tmp.getManager().getUsername().equals(getCurrentManager().getUsername())
                    && tmp.getIsCurrently()) {

                return tmp.getManager();
            }
        }
        for (ManagerTeam tmp : matchTeam.getVisitTeam().getManagerTeams()) {

            if (!tmp.getManager().getUsername().equals(getCurrentManager().getUsername())
                    && tmp.getIsCurrently()) {

                return tmp.getManager();
            }
        }

        return null;

    }


    public List<MatchTeam> getCurrentMatches() {

        List<MatchTeam> listOfCurrentMatches = new ArrayList<>();


        Manager manager = getCurrentManager();


        Team team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam).orElse(null);

        if (team != null) {
            listOfCurrentMatches.addAll(team.getHomeGames());
            listOfCurrentMatches.addAll(team.getVisitGames());
            listOfCurrentMatches.sort(Comparator.comparing(MatchTeam::getQueue));
            return listOfCurrentMatches;
        }

        return new ArrayList<>();


    }

    public List<Team> getCurrentEnemy() {

        List<MatchTeam> currentMatches = getCurrentMatches();


        List<Team> listOfCurrentEnemy = new ArrayList<>();

        for (MatchTeam tmp : currentMatches) {
            if (tmp.getHomeTeam().equals(getCurrentManagerTeam())) {
                listOfCurrentEnemy.add(tmp.getVisitTeam());
            }
            if (tmp.getVisitTeam().equals(getCurrentManagerTeam())) {
                listOfCurrentEnemy.add(tmp.getHomeTeam());
            }
        }


        return listOfCurrentEnemy;


    }



    public Optional<Team> getCurrentPlayersOfTeam() {


        Manager manager = getCurrentManager();


        System.out.println(manager.getUsername());

        Optional<Team> team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam);


        return team;

    }

 /*   public Long getNotRead() {

        List<Message> getAllNoDeletedMessages = getCurrentManager()
                .getMessagesGot().stream()
                .filter(p -> !p.getIsDeleteByReceiver())
                .collect(Collectors.toList());

        return getAllNoDeletedMessages.stream().filter(p -> !p.getIsRead()).count();
    }*/

       public Long getNotRead() {

        List<Message> getAllNoDeletedMessages = getCurrentUser()
                .getMessagesGot().stream()
                .filter(p -> !p.getIsDeleteByReceiver())
                .collect(Collectors.toList());

        return getAllNoDeletedMessages.stream().filter(p -> !p.getIsRead()).count();
    }
    public Long getAllMessage() {

        List<Message> getAllNoDeletedMessages = getCurrentUser()
                .getMessagesGot().stream()
                .filter(p -> !p.getIsDeleteByReceiver())
                .collect(Collectors.toList());

        return getAllNoDeletedMessages.stream().count();
    }
}
