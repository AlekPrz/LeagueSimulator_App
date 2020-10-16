package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.TeamException;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.TeamValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
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
        for (Contract contract : contractService.getAllContracts()) {
            if (contract.getTeam().getId().equals(id)) {
                contractService.deleteContract(contract);
            }
        }
        for (ManagerTeam managerTeam : managerTeamRepository.findAll()) {
            if (managerTeam.getTeam().getId().equals(id)) {
                managerTeamRepository.delete(managerTeam);
            }
        }
        teamRepository.deleteById(id);
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

}