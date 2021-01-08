package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Enum.StatusOfMatch;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Message;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MessageRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")

public class ManagerControllerGameplay {

    private final MatchTeamRepository matchTeamRepository;
    private final ManagerService managerService;
    private final MessageRepository messageRepository;

    //moge dodać wysyłanie wiadomośći
    @GetMapping("/terminarzDruzyny/zmienDateMeczu/{id}")
    public String getDate(@PathVariable Long id, Model model) {


        model.addAttribute("matchTeam", matchTeamRepository.findById(id).orElse(null));


        return "manager/newSchedOfMatch";

    }


    @PostMapping("/terminarzDruzyny/zapronujZmianeMeczu")
    public String getDate(@Valid @ModelAttribute MatchTeam matchTeam, BindingResult bindingResult, Model model, String newDate) {


        Map<String, String> errors = new LinkedHashMap<>();
        MatchTeam matchTeamToSave = matchTeamRepository.findById(matchTeam.getId()).orElse(null);

        MatchTeam nextMatch = matchTeamRepository
                .findAll()
                .stream()
                .filter(p -> p.getQueue() == matchTeamToSave.getQueue() + 1)
                .findFirst()
                .orElse(null);


        LocalDate currentlyDate = matchTeamRepository.findById(matchTeam.getId()).get().getDateOfGame();
        LocalDate newDateParse = null;


        if (newDate.isEmpty() || newDate.isBlank()) {
            errors.put("DateError", "Nowa data jest pusta");
        }



        if (!newDate.isEmpty() && errors.isEmpty()) {
            newDateParse = LocalDate.parse(newDate);

            if (newDateParse.compareTo(currentlyDate) == 0) {
                errors.put("DateError", "Data nie może być taka sama!");
            } else if (newDateParse.compareTo(LocalDate.now()) < 0) {
                errors.put("DateError", "Data nie może być wcześniejsza niż dzień dzisiejszy!");
            }
        }


        if (nextMatch != null && errors.isEmpty()) {
            if (newDateParse.compareTo(nextMatch.getDateOfGame()) > 0 || newDateParse.compareTo(currentlyDate.minusWeeks(2)) < 0) {
                errors.put("DateError",
                        "Data nie może być późniejsza niż następny mecz");
            }

        }

        if (!errors.isEmpty()) {
            model.addAttribute("matchTeam", matchTeam);
            model.addAttribute("errors", errors);
            return "manager/newSchedOfMatch";
        }


        if (matchTeamToSave.getStatusOfMatch().equals(StatusOfMatch.SCHEDULED)) {

            Manager enemy = managerService.getCurrentEnemy(matchTeamToSave);

            messageRepository.save
                    (Message.builder()
                            .text("Przeciwnik zaproponował Ci zmiane daty rozegrania meczu, jeśli chciałbyś tego dokonać, wejdź w terminarz  " +
                                    " swojej drużyny i zatwierdź zmianę, zaproponowany termin to: " + newDate)
                            .dateOfSend(LocalDate.now())
                            .isDeleteByReceiver(false)
                            .isDeletedBySender(false)
                            .isRead(false)
                            .userReceiver(enemy)
                            .userSender(managerService.getCurrentManager())
                            .subject("Zmiana terminu meczu").build());


            matchTeamToSave.setStatusOfMatch(StatusOfMatch.INCOME);
            matchTeamToSave.setNewDateOfGame(LocalDate.parse(newDate));
            matchTeamToSave.setManagerProposer(managerService.getCurrentManager());
            matchTeamToSave.setManagerReceiver(enemy);

            matchTeamRepository.save(matchTeamToSave);

        }


        return "redirect:/manager/terminarzDruzyny";

    }

    @PostMapping("/terminarzDruzyny/odrzucZmiane")
    public String deleteChange(Long id) {


        if (id != null) {
            Optional<MatchTeam> matchTeamOptional = matchTeamRepository.findById(id);

            if (matchTeamOptional.isPresent()) {
                MatchTeam matchTeam = matchTeamOptional.get();
                matchTeam.setNewDateOfGame(null);
                matchTeam.setStatusOfMatch(StatusOfMatch.DECLINE);
                matchTeamRepository.save(matchTeam);
            }
        }

        return "redirect:/manager/terminarzDruzyny";

    }

    @PostMapping("/terminarzDruzyny/zatwierdzZmiane")
    public String acceptChange(Long id) {


        if (id != null) {
            Optional<MatchTeam> matchTeamOptional = matchTeamRepository.findById(id);

            if (matchTeamOptional.isPresent()) {
                MatchTeam matchTeam = matchTeamOptional.get();
                matchTeam.setDateOfGame(matchTeam.getNewDateOfGame());
                matchTeam.setStatusOfMatch(StatusOfMatch.ACCEPTED);
                matchTeamRepository.save(matchTeam);
            }
        }

        return "redirect:/manager/terminarzDruzyny";

    }


}
