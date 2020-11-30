package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;


    public List<Contract> getAllContracts() {
        return contractRepository
                .findAll();
    }

    public void deleteContract(Contract contract){
        contractRepository.delete(contract);
    }

    public List<Player> getAllPlayersIdsFromTeam(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Cannot find players from team with id " + teamId);
        }
        return contractRepository.getAllPlayersIdsFromTeam(teamId);
    }
}
