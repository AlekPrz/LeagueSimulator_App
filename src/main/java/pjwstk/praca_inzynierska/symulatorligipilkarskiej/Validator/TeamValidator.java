package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Validator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamRepository teamRepository;
    Map<String, String> errors = new HashMap<>();

    public Map<String, String> validate(Team team) {


        errors.clear();

        System.out.println(team.getName());

        if (!isNameValid(team.getName())) {

            errors.put("Team", "Nazwa drużyny nie może być z małych liter");
            throw new RuntimeException("Ten gracz ma już druyżne");


        }
        if (!teamExist(team.getName())) {
            errors.put("Team", "Drużyna znajduje się w bazie");
        }

        return errors;


    }



    private boolean isNameValid(String name) {
        return Objects.nonNull(name) && name.matches("[A-Z]+");
    }

    private boolean teamExist(String name) {
        return teamRepository.findByName(name).isEmpty();
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }


}
