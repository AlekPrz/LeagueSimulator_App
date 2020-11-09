package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.TeamValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamValidator teamValidator;
    private final ContractService contractService;
    private final ManagerService managerService;
    private final SeasonTeamRepository seasonTeamRepository;
    private final MatchTeamRepository matchTeamRepository;

    private final ManagerTeamRepository managerTeamRepository;


    public Map<String, String> checkErrors(Team team, ManagerTeam managerTeam, BindingResult bindingResult) {

        Map<String, String> errorsFromBinding
                = bindingResult
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (v1, v2) -> v1 + ", " + v2
                ));

        Map<String, String> errorsFromMyValidate = new LinkedHashMap<>();
        errorsFromMyValidate.putAll(teamValidator.validate(team, managerTeam));
        errorsFromBinding.forEach(errorsFromMyValidate::putIfAbsent);

        return errorsFromMyValidate;

    }


    public Team createTeam(Team team, ManagerTeam managerTeam) {

        Manager manager = managerTeam.getManager();
        teamRepository.save(team);
        ManagerTeam managerTeam1 = ManagerTeam.builder()
                .isCurrently(true)
                .endOfContract(managerTeam.getEndOfContract())
                .startOfContract(managerTeam.getStartOfContract())
                .team(team)
                .manager(manager)
                .build();


        managerTeamRepository.save(managerTeam1);


        manager.getManagerTeams().add(managerTeam);
        team.getManagerTeams().add(managerTeam);
        return team;

    }

    public void deleteTeam(Long id) {


        Team teamToDelete = teamRepository.findById(id).orElse(null);


        for (SeasonTeam tmp : seasonTeamRepository.findAll()) {
            if (tmp.getTeam().getId().equals(teamToDelete.getId())) {
                seasonTeamRepository.delete(tmp);

            }
        }
        for (MatchTeam tmp : matchTeamRepository.findAll()) {
            if (tmp.getHomeTeam().getId().equals(teamToDelete.getId())) {
                matchTeamRepository.delete(tmp);
            }
            if (tmp.getVisitTeam().getId().equals(teamToDelete.getId())) {
                matchTeamRepository.delete(tmp);
            }
        }


        teamRepository.delete(teamToDelete);


    }


    public List<Team> getAllTeam() {
        return teamRepository
                .findAll();
    }

    public List<Team> findByKeyword(String keyword) {
        return teamRepository.findByKeyword(keyword);
    }


    public List<Player> getAllPlayersInThatTeam(Long id) {


        List<Player> playersInThatTeam = new ArrayList<>();

        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            throw new RuntimeException("team is null");
        }


        for (Contract tmp : team.getContracts()) {
            if (tmp.getIsCurrently()) {
                playersInThatTeam.add(tmp.getPlayer());
            }
        }

        return playersInThatTeam;

    }

    public ManagerTeam findCurrentlyManager(Long id) {

        Team team = teamRepository.findById(id).orElse(null);


        ManagerTeam managerTeam = null;

        for (ManagerTeam tmp : team.getManagerTeams()) {
            if (tmp.getIsCurrently()) {
                managerTeam = tmp;
            }
        }


        return managerTeam;
    }

}