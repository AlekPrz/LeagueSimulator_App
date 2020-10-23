package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamRepository teamRepository;
    private final ManagerTeamRepository managerTeamRepository;
    private final ManagerService managerService;
    Map<String, String> errorsTeam = new HashMap<>();

    public Map<String, String> validate(Team team, ManagerTeam managerTeam) {


        errorsTeam.clear();


        if (!teamExistByName(team.getName())) {
            errorsTeam.put("TeamName", "Drużyna o takiej nazwie znajduje się w bazie");
        }
        if (!teamExistByShortName(team.getShortName())) {
            errorsTeam.put("TeamShortName", "Drużyna o takiej krótkiej nazwie znajduje się w bazie");
        }

        if (managerTeam.getManager() == null) {
            errorsTeam.put("ManagerNull", "Drużyna musi mieć managera !");
        }

        if(managerTeam.getEndOfContract() == null || managerTeam.getStartOfContract() == null){
            errorsTeam.put("Data", "Data nie może pozostać pusta");

        }



;
        Manager manager = managerService.findManagerById(managerTeam.getManager().getId());


        for (ManagerTeam mt : manager.getManagerTeams()) {
            if (mt.getIsCurrently()) {
                errorsTeam.put("ManagerCurrently", "Ten manager ma już drużyne, nie może mieć 2 drużyn!");
            }

        }
        return errorsTeam;


    }


    private boolean teamExistByName(String name) {
        return teamRepository.findByName(name).isEmpty();
    }
    private boolean teamExistByShortName(String name) {
        return teamRepository.findByShortName(name).isEmpty();
    }


    public boolean hasErrors() {
        return !errorsTeam.isEmpty();
    }

    public Map<String, String> getErrors() {
        return errorsTeam;
    }
}
