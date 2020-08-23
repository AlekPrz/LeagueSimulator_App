package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Season;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class MatchTeam {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "hosting_team_id")
    private Team homeTeam;
    @ManyToOne
    @JoinColumn(name = "visit_team_id")
    private Team visitTeam;
    private String score;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;


}
