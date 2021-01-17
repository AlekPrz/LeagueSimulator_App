package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PlayerValidator {

    private final UserRepository<Player> playerUserRepository;
    private final TeamService teamService;
    Map<String, String> errorsPlayer = new HashMap<>();

    public Map<String, String> validate(Player player, Contract contract) {


        errorsPlayer.clear();


        if (!playerExist(player.getShirtName())) {
            errorsPlayer.put("shirtName", "Gracz o takiej nazwie znajduje się już w bazie!");
        }

        if (contract.getStartOfContract() == null) {
            errorsPlayer.put("DataStart", "Data nie może pozostać pusta");
        }

        if (player.getAge() == null) {
            errorsPlayer.put("ageNull", "Wiek nie może być pusty");

        }

        int w = 0;


        if (teamService.getAllPlayersInThatTeam(contract.getTeam().getId()) != null) {
            w = teamService.getAllPlayersInThatTeam(contract.getTeam().getId()).size();
        }

        if (w == 3) {
            errorsPlayer.put("player2Much", "W drużynie jest za dużo graczy może być maksymalnie 3!");

        }
        return errorsPlayer;


    }


    public Map<String, String> validateModify(Player player, Contract contract) {


        errorsPlayer.clear();


        if (!playerExist(player.getShirtName()) && !player.getShirtName().equals(playerUserRepository.findById(player.getId()).orElse(null).getShirtName())) {
            errorsPlayer.put("shirtName", "Gracz o takiej nazwie znajduje się już w bazie!");
        }

        if (contract.getStartOfContract() == null) {
            errorsPlayer.put("DataStart", "Data nie może pozostać pusta");
        }

        if (player.getAge() == null) {
            errorsPlayer.put("ageNull", "Wiek nie może być pusty");

        }


        return errorsPlayer;


    }


    private boolean playerExist(String name) {
        return playerUserRepository.findByShirtName(name).isEmpty();
    }

    public boolean hasErrors() {
        return !errorsPlayer.isEmpty();
    }


}
