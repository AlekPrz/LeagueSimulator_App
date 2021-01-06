package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.TeamException;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.PlayerValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator.TeamValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.PasswordGenerator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.ContractRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final UserRepository<Player> playerUserRepository;
    private final PlayerValidator playerValidator;
    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    private final ManagerService managerService;


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


        errorsFromBinding.forEach(errorsFromMyValidate::putIfAbsent);

        return errorsFromMyValidate;

    }

    public Map<String, String> checkErrorsModify(Player player, Contract contract, BindingResult bindingResult) {

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
        errorsFromMyValidate.putAll(playerValidator.validateModify(player, contract));


        errorsFromBinding.forEach(errorsFromMyValidate::putIfAbsent);

        return errorsFromMyValidate;

    }


    public Player modifyPlayer(Player player, Contract contract) {

        Team team = contract.getTeam();
        Contract contract2 = Contract.builder()
                .startOfContract(contract.getStartOfContract())
                .player(player)
                .team(team)
                .goals(0L)
                .isCurrently(true)
                .build();


        if (playerChangedHisTeam(player.getId(), team.getId())) {
            Contract contractCurrently = findCurrentlyContract(player.getId());
            contractCurrently.setIsCurrently(false);
            contractRepository.save(contractCurrently);
            contractRepository.save(contract2);
            return player;
        }

        if (findCurrentlyContract(player.getId()) == null) {
            team.getContracts().add(contract2);
            player.getContracts().add(contract2);
            contractRepository.save(contract2);
            playerUserRepository.save(player);

            return player;
        }


        playerUserRepository.save(player);
        return player;
    }


    public Player createPlayer(Player player, Contract contract) {


        Team team = contract.getTeam();


        Contract contract2 = Contract.builder()
                .startOfContract(contract.getStartOfContract())
                .player(player)
                .team(team)
                .goals(0L)
                .isCurrently(true)
                .build();


        player.setRole(Role.PLAYER);
        player.setUsername(player.getShirtName());


        playerUserRepository.save(player);
        contractRepository.save(contract2);
        team.getContracts().add(contract2);
        player.getContracts().add(contract2);


        return player;
    }


    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();

        for (User tmp : playerUserRepository.findAll()) {

            if (tmp.getRole().getDescription().equals("ROLE_PLAYER")) {
                players.add((Player) tmp);
            }
        }

        return players;

    }


    public void deletePlayer(Long id) {
        for (Contract contract : contractRepository.findAll()) {
            if (contract.getPlayer().getId().equals(id)) {

                contractRepository.delete(contract);
            }
        }


        playerUserRepository.deleteById(id);
    }


    public Player findPlayerById(Long id) {
        return playerUserRepository.findById(id).orElse(null);
    }


    public boolean playerChangedHisTeam(Long playerId, Long teamId) {

        Player player = findPlayerById(playerId);
        Team team = teamRepository.findById(teamId).orElse(null);


        for (Contract tmp : player.getContracts()) {
            if (tmp.getIsCurrently() && !tmp.getTeam().getName().equals(team.getName())) {
                return true;
            }
        }
        return false;

    }

    public Contract findCurrentlyContract(Long id) {

        Player player = findPlayerById(id);
        Contract contract = null;

        for (Contract tmp : player.getContracts()) {
            if (tmp.getIsCurrently()) {
                contract = tmp;
            }
        }


        return contract;
    }

    public List<MatchTeam> findMyMatches() {
        Player player = (Player) managerService.getCurrentUser();

        Contract contract = findCurrentlyContract(player.getId());

        List<MatchTeam> matchTeamForPlayers = new ArrayList<>();

        matchTeamForPlayers.addAll(contract.getTeam().getHomeGames());
        matchTeamForPlayers.addAll(contract.getTeam().getVisitGames());
        matchTeamForPlayers.sort(Comparator.comparing(MatchTeam::getQueue));

        return matchTeamForPlayers;


    }


}
