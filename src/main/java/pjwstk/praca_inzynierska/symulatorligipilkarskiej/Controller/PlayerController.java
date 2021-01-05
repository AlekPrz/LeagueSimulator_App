package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.Counter;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;

import java.util.List;

@Controller
@RequestMapping("/zawodnik")
@RequiredArgsConstructor

public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/mojeMecze")
    public String getSchedule(Model model) {


        System.out.println("witam");
        List<MatchTeam> getMyTeamMatches = playerService.findMyMatches();

        model.addAttribute("myMatches", getMyTeamMatches);

        return "player/myMatches";
    }
}
