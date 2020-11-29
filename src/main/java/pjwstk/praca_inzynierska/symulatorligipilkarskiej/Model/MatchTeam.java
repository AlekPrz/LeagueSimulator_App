package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Season;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class MatchTeam {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;


    @ManyToOne
    @JoinColumn(name = "hosting_team_id")
    private Team homeTeam;

    @ManyToMany(mappedBy = "matchTeamsHome")
    private Set<Player> homeTeamPlayers;

    @ManyToMany(mappedBy = "matchTeamsVisit")
    private Set<Player> visitTeamPlayers;

    @ManyToOne
    @JoinColumn(name = "visit_team_id")
    private Team visitTeam;

    @EqualsAndHashCode.Exclude
    @Pattern(regexp = "[1-9]-[1-9]", message = " Wynik musi być w formacie liczba-liczba, (minusowe liczby nie są akceptowalne) ")
    private String score;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;


    @EqualsAndHashCode.Exclude
    private Integer queue;


    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfGame;

    public boolean isDuplicate(MatchTeam matchTeam) {

        if (homeTeam.equals(matchTeam.getHomeTeam()) && visitTeam.equals(matchTeam.getVisitTeam())) {


            return true;
        } else if (homeTeam.equals(matchTeam.getVisitTeam()) && visitTeam.equals(matchTeam.getHomeTeam())) {


            return true;
        }
        return false;
    }

    public static List<MatchTeam> oldMatch = new ArrayList<>();


}
