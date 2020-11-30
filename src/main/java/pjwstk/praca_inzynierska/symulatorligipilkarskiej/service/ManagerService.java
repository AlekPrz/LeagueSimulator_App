package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository<Manager> managerUserRepository;



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

    public Manager getCurrentManager() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Manager> manager = Optional.empty();

        if (principal instanceof UserDetails) {

            String usernameName = ((UserDetails) principal).getUsername();
            manager = managerUserRepository.findManagerByUsername(usernameName);

        }


        return manager.orElse(null);

    }

    public List<MatchTeam> getCurrentMatches() {

        List<MatchTeam> listOfCurrentMatches = new ArrayList<>();


        Manager manager = getCurrentManager();
        Team team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam).orElse(null);


        listOfCurrentMatches.addAll(team.getHomeGames());
        listOfCurrentMatches.addAll(team.getVisitGames());

        listOfCurrentMatches.sort(Comparator.comparing(MatchTeam::getQueue));

        return listOfCurrentMatches;


    }

    public Team getCurrentPlayersOfTeam() {


        Manager manager = getCurrentManager();

        Team team =
                manager.getManagerTeams().stream().filter(p -> p.getIsCurrently().equals(true))
                        .findFirst().map(ManagerTeam::getTeam).orElse(null);




        return team;


    }

}
