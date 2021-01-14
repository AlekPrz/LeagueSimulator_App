package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
/*
@RequiredArgsConstructor
*/
public class TeamValidator {

    private final TeamRepository teamRepository;
    private final ManagerService managerService;
    private final TeamService teamService;
    Map<String, String> errorsTeam = new HashMap<>();

    public TeamValidator(TeamRepository teamRepository, ManagerService managerService, @Lazy TeamService teamService) {
        this.teamRepository = teamRepository;
        this.managerService = managerService;
        this.teamService = teamService;
    }

    public Map<String, String> validate(Team team, ManagerTeam managerTeam) {


        errorsTeam.clear();


        System.out.println(teamRepository.findAll().size());

        if (!teamExistByName(team.getName())) {
            errorsTeam.put("TeamName", "Drużyna o takiej nazwie znajduje się w bazie");
        }
        if (!teamExistByShortName(team.getShortName())) {
            errorsTeam.put("TeamShortName", "Drużyna o takiej krótkiej nazwie znajduje się w bazie");
        }

        if (managerTeam.getManager() == null) {
            errorsTeam.put("ManagerNull", "Drużyna musi mieć managera !");
        }

        if (managerTeam.getEndOfContract() == null || managerTeam.getStartOfContract() == null) {
            errorsTeam.put("Data", "Data nie może pozostać pusta");

        }

        if(teamRepository.findAll().size()>5){
            errorsTeam.put("Team2Much", "W bazie jest za dużo drużyn, maksymalnie może być 6 drużyn");
        }


        Manager manager = managerService.findManagerById(managerTeam.getManager().getId());


        for (ManagerTeam mt : manager.getManagerTeams()) {
            if (mt.getIsCurrently()) {
                errorsTeam.put("ManagerCurrently", "Ten manager ma już drużyne, nie może mieć 2 drużyn!");
            }

        }
        return errorsTeam;


    }


    public Map<String, String> validateModify(Team team, ManagerTeam managerTeam) {


        errorsTeam.clear();

        Team currentlyTeam = teamService.findCurrentlyManagerTeam(team.getId()).getTeam();



        if (!teamExistByName(team.getName()) && !team.getName().equals(currentlyTeam.getName())) {
            errorsTeam.put("TeamName", "Drużyna o takiej nazwie znajduje się w bazie");
        }
        if (!teamExistByShortName(team.getShortName()) && !team.getShortName().equals(currentlyTeam.getShortName())) {
            errorsTeam.put("TeamShortName", "Drużyna o takiej krótkiej nazwie znajduje się w bazie");
        }


        if (managerTeam.getEndOfContract() == null || managerTeam.getStartOfContract() == null) {
            errorsTeam.put("Data", "Data nie może pozostać pusta");

        }


        Manager manager = managerService.findManagerById(managerTeam.getManager().getId());


        for (ManagerTeam mt : manager.getManagerTeams()) {
            if (mt.getIsCurrently() &&
                    !teamService.findCurrentlyManagerTeam
                            (team.getId()).getManager().getUsername()
                            .equals(mt.getManager().getUsername())) {
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
