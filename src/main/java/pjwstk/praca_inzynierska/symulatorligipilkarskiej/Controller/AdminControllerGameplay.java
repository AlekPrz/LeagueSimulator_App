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
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ContractService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.MatchTeamService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminControllerGameplay {

    private final MatchTeamService matchTeamService;
    private final MatchTeamRepository matchTeamRepository;
    private final TeamRepository teamRepository;
    private final ContractService contractService;


    @GetMapping("/terminarz")
    public String getSchedule(@PageableDefault(size = 2) Pageable pageable, Model model) {





        Page<MatchTeam> page = matchTeamRepository.findAllByOrderByQueue(pageable);

        if (matchTeamRepository.findAll().isEmpty()) {
            model.addAttribute("emptyMatch", true);
        }
        if (teamRepository.findAll().size() < 3) {
            System.out.println("ja?");
            model.addAttribute("size2low", true);
            model.addAttribute("sizeOfTeams", teamRepository.findAll().size());
        }
        if (teamRepository.findAll().size() > 6) {
            System.out.println("Jaa?");
            model.addAttribute("size2big", true);
            model.addAttribute("sizeOfTeams", teamRepository.findAll().size());
        }
        if(!contractService.ifEnoughPlayersInTeam()){
            model.addAttribute("sizeOfPlayers2Low", true);
        }


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

    @PostMapping("/zmienDateMeczu")
    public String postDate(@Valid @ModelAttribute MatchTeam matchTeam, BindingResult bindingResult, Model model, String newDate) {


        Map<String, String> errors = new LinkedHashMap<>();

        MatchTeam matchTeamToSave = matchTeamRepository.findById(matchTeam.getId()).get();

        MatchTeam nextMatch = matchTeamRepository
                .findAll()
                .stream()
                .filter(p -> p.getQueue() == matchTeamToSave.getQueue() + 1)
                .findFirst()
                .orElse(null);

        System.out.println("3");


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

            } else if (nextMatch != null && (newDateParse.compareTo(nextMatch.getDateOfGame()) >= 0 || newDateParse.compareTo(currentlyDate.minusWeeks(2)) <= 0)) {
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
