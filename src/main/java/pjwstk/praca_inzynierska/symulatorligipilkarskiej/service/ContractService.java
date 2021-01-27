package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;

    public List<Contract> getAllContracts() {
        return contractRepository
                .findAll();
    }

    public void deleteContract(Contract contract) {
        contractRepository.delete(contract);
    }

    public List<Player> getAllPlayersIdsFromTeam(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Cannot find players from team with id " + teamId);
        }
        return contractRepository.getAllPlayersIdsFromTeam(teamId);
    }

    //Znaleźć ile jest graczy w drużynach
    public boolean ifEnoughPlayersInTeam() {
        List<Integer> integers = new ArrayList<>();
        Integer w = 0;
        List<Team> teams = new ArrayList<>();
        List<Team> allTeamsInbase = teamRepository.findAll();


        for (Contract tmp : contractRepository.findAll()) {
            int twoAdd = 0;
            if (!teams.contains(tmp.getTeam())) {
                for (Contract tmp1 : tmp.getTeam().getContracts()) {
                    if (tmp1.getIsCurrently()) {
                        twoAdd++;
                    }
                    teams.add(tmp.getTeam());
                }
                integers.add(twoAdd);
            }
        }

        for (Team tmp : allTeamsInbase) {
            if (!teams.contains(tmp)) {
                teams.add(tmp);
                integers.add(0);
            }
        }
        for (Integer tmp : integers) {
            if (tmp < 2) {
                return false;
            }
        }

        return true;
    }
}
