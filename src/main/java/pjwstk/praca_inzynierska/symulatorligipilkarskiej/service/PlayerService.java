package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.TeamException;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.PlayerValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.TeamValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.PasswordGenerator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final UserRepository<Player> playerUserRepository;
    private final PlayerValidator playerValidator;
    private final ContractRepository contractRepository;

    public Player createPlayer(Player player, Contract contract) {


/*
        var errors = playerValidator.validate(player);
*/

        Team team = contract.getTeam();


        Contract contract1 =
                Contract.builder()
                        .endOfContract(contract.getEndOfContract())
                        .startOfContract(contract.getStartOfContract())
                        .player(player)
                        .team(team)
                        .goals(0L)
                        .salary(contract.getSalary()).build();
/*
        if (playerValidator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Bład przy tworzeniu gracza " + errorsMessage);
        }*/


        playerUserRepository.save(player);
        contractRepository.save(contract1);
        team.getContracts().add(contract1);
        player.getContracts().add(contract1);

        String password = PasswordGenerator.stringGenerator();

        Player player1 = Player.builder()
                .username(player.getName().substring(0, 1).concat(player.getSurname()))
                .password(UserRegister.encodePassword(password))
                .repeatPassword(UserRegister.encodePassword(password))
                .role(Role.PLAYER).build();

        return player;


    }

    public List<Player> getAllPlayers() {
        return playerUserRepository
                .findAll();
    }


    public void deletePlayer(Long id) {
        for (Contract contract : contractRepository.findAll()) {
            if (contract.getPlayer().getId().equals(id)) {
                contractRepository.delete(contract);
                contract.getTeam().getContracts().remove(contract);
            }
        }


        playerUserRepository.deleteById(id);
    }
}