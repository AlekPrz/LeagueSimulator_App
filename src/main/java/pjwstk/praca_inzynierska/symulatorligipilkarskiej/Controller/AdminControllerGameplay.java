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
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.MatchTeamService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminControllerGameplay {

    private final MatchTeamService matchTeamService;
    private final SeasonRepository seasonRepository;
    private final MatchTeamRepository matchTeamRepository;
    private final SeasonTeamRepository seasonTeamRepository;


    @GetMapping("/terminarz")
    public String getSchedule(@PageableDefault(size = 2) Pageable pageable, Model model, HttpServletRequest request) {


        Page<MatchTeam> page = matchTeamRepository.findAllByOrderByQueue(pageable);


        if (matchTeamRepository.findAll().isEmpty()) {
            model.addAttribute("emptyMatch", true);
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


        matchTeamRepository.save(matchTeam);


        return "redirect:/admin/terminarz";

    }


    @PostMapping("/generujTerminarz")
    public String postScheduleGenerate() {


        matchTeamService.generateSchedule();

        return "redirect:/admin/terminarz";

    }

    @PostMapping("/usuńTerminarz")
    public String postScheduleDelete() {

        matchTeamRepository.deleteAll();
        seasonRepository.deleteAll();

        return "redirect:/admin/terminarz";

    }


}
