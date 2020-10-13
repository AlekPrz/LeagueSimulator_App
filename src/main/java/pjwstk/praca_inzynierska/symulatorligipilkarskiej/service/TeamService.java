package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.TeamException;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.TeamValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ManagerTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamValidator teamValidator;
    private final ContractService contractService;
    private final ManagerService managerService;

    private final ManagerTeamRepository managerTeamRepository;

    public Team createTeam(Team team) {

      /*  var errors = teamValidator.validate(team);
        if (teamValidator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Bład przy tworzeniu drużyny " + errorsMessage);
        }
*/
        teamRepository.save(team);
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


}