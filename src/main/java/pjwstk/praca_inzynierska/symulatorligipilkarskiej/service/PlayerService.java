package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final UserRepository<Player> playerUserRepository;
    private final PlayerValidator playerValidator;
    private final ContractRepository contractRepository;


    public Map<String, String> checkErrors(Player player, Contract contract, BindingResult bindingResult) {

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
        errorsFromMyValidate.putAll(playerValidator.validate(player, contract));

        System.out.println(errorsFromMyValidate);

        errorsFromBinding.forEach(errorsFromMyValidate::putIfAbsent);

        return errorsFromMyValidate;

    }


    public Player createPlayer(Player player, Contract contract) {


        Team team = contract.getTeam();


        Contract contract1 =
                Contract.builder()
                        .endOfContract(contract.getEndOfContract())
                        .startOfContract(contract.getStartOfContract())
                        .player(player)
                        .team(team)
                        .goals(0L)
                        .isCurrently(true)
                        .build();

        playerUserRepository.save(player);
        contractRepository.save(contract1);
        team.getContracts().add(contract1);
        player.getContracts().add(contract1);

     /*   String password = PasswordGenerator.stringGenerator();

        Player player1 = Player.builder()
                .username(player.getName().substring(0, 1).concat(player.getSurname()))
                .password(UserRegister.encodePassword(password))
                .repeatPassword(UserRegister.encodePassword(password))
                .role(Role.PLAYER).build();
*/
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


    public Player findPlayerById(Long id) {
        return playerUserRepository.findById(id).orElse(null);
    }


    public Contract findCurrentlyContract(Long id) {

        Player player = findPlayerById(id);
        Contract contract = null;

        for (Contract tmp : player.getContracts()) {
            if (tmp.getIsCurrently()) {
                contract = tmp;
            }
        }

        if (contract == null) {
            throw new RuntimeException("Contract is null");
        } else {
            return contract;
        }

    }

}