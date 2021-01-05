package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.MatchTeamService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminControllerGameplay {

    private final MatchTeamService matchTeamService;
    private final MatchTeamRepository matchTeamRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;


    @GetMapping("/terminarz")
    public String getSchedule(Model model) {


        Page<MatchTeam> page = matchTeamRepository.findAllByOrderByQueue(PageRequest.of(0, teamService.getAllTeam().size() / 2));

        if (matchTeamRepository.findAll().isEmpty()) {
            model.addAttribute("emptyMatch", true);
        }
       /* if (teamRepository.findAll().size()<5) {
            model.addAttribute("size2low", true);
        }*/

        model.addAttribute("page", page);

        return "admin/schedule/schedule";
    }

    @GetMapping("/wprowadźWynik/{id}")
    public String getInsert(@PathVariable Long id, Model model) {


        model.addAttribute("matchTeam", matchTeamRepository.findById(id).orElse(null));


        return "admin/schedule/scheduleInsertScore";

    }

    @PostMapping("/wprowadźWynik")
    public String postInsert(@Valid @ModelAttribute MatchTeam matchTeam, BindingResult bindingResult, Model model) {


        Map<String, String> errorsFromBinding
                = bindingResult
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (v1, v2) -> v1 + ", " + v2
                ));


        if (!errorsFromBinding.isEmpty()) {
            model.addAttribute("error", errorsFromBinding);
            model.addAttribute("matchTeam", matchTeam);
            return "admin/schedule/scheduleInsertScore";
        }

        matchTeamService.generateTable(matchTeam);

        return "redirect:/admin/terminarz";

    }


    @GetMapping("/zmienDateMeczu/{id}")
    public String getDate(@PathVariable Long id, Model model) {


        model.addAttribute("matchTeam", matchTeamRepository.findById(id).orElse(null));


        return "admin/schedule/scheduleChangeDate";

    }

    //Pobierz date natepnego meczu po prostu....
    @PostMapping("/zmienDateMeczu")
    public String postDate(@Valid @ModelAttribute MatchTeam matchTeam, BindingResult bindingResult, Model model, String newDate) {


        Map<String, String> errors = new LinkedHashMap<>();

        MatchTeam matchTeamToSave = matchTeamRepository.findById(matchTeam.getId()).get();


        LocalDate nextMatch = Objects.requireNonNull(matchTeamRepository
                .findAll()
                .stream()
                .filter(p -> p.getQueue() == matchTeamToSave.getQueue() + 1)
                .findFirst()
                .orElse(null))
                .getDateOfGame();


        LocalDate currentlyDate = matchTeamRepository.findById(matchTeam.getId()).get().getDateOfGame();
        LocalDate newDateParse = null;

        if (!newDate.isEmpty()) {
            newDateParse = LocalDate.parse(newDate);
        }


        if (newDateParse != null) {
            if (newDateParse.compareTo(currentlyDate) == 0) {
                errors.put("DateError", "Data nie może być taka sama!");
                System.out.println("1");
            } else if (newDateParse.compareTo(LocalDate.now()) < 0) {
                errors.put("DateError", "Data nie może być wcześniejsza niż dzień dzisiejszy!");
                System.out.println("2");

            } else if (newDateParse.compareTo(nextMatch) > 0 ||
                    newDateParse.compareTo(currentlyDate.minusWeeks(2)) < 0) {
                errors.put("DateError",
                        "Data nie może być późniejsza niż następny mecz");
            }
            matchTeamToSave.setDateOfGame(newDateParse);


            if (!errors.isEmpty()) {
                model.addAttribute("matchTeam", matchTeam);
                model.addAttribute("errors", errors);

                return "admin/schedule/scheduleChangeDate";
            }
            matchTeamRepository.save(matchTeamToSave);


        }


        return "redirect:/admin/terminarz";

    }

    //Do poprawie

    @PostMapping("/generujTerminarz")
    public String postScheduleGenerate() {





        matchTeamService.generateSchedule();
        return "redirect:/admin/terminarz";

    }

    @PostMapping("/usuńTerminarz")
    public String postScheduleDelete() {
        matchTeamService.deleteSchedule();
        return "redirect:/admin/terminarz";
    }


}
