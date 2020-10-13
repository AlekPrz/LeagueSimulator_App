package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
    Map<String, String> errors = new HashMap<>();

    public Map<String, String> validate(Player player) {


        errors.clear();


        if (!isNameValid(player.getName()) || !isNameValid(player.getSurname())) {

            errors.put("Player", "Gracz nie może mieć nazw z małych liter!");

        }
        if (playerExist(player.getShirtName())) {
            errors.put("Player", "Gracz znajduje się w bazie!");
        }

        return errors;


    }



    private boolean isNameValid(String name) {
        return Objects.nonNull(name) && name.matches("[A-Z]+");
    }

    private boolean playerExist(String name) {
        return playerUserRepository.findByShirtName(name).isEmpty();
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }


}
