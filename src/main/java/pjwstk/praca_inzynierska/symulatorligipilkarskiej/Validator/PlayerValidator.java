package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PlayerValidator {

    private final UserRepository<Player> playerUserRepository;
    Map<String, String> errorsPlayer = new HashMap<>();

    public Map<String, String> validate(Player player, Contract contract) {


        errorsPlayer.clear();


        if (!playerExist(player.getShirtName()))
        {
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




    public Map<String, String> validateModify(Player player, Contract contract) {



        errorsPlayer.clear();


        if (!playerExist(player.getShirtName()) &&
                !player.getShirtName().equals(playerUserRepository.findById(player.getId()).orElse(null).getShirtName()))
        {
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
