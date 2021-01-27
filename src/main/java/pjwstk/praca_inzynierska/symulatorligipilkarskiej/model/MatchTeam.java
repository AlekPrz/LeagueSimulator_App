package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Enum.StatusOfMatch;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;

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

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "matchTeamsHome", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<Player> homeTeamPlayers;

    public void removeMatchTeamHome(Player player) {
        this.homeTeamPlayers.remove(player);
        player.getMatchTeamsHome().remove(this);
    }

    public void removeMatchTeamVisit(Player player) {
        this.visitTeamPlayers.remove(player);
        player.getMatchTeamsVisit().remove(this);
    }

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "matchTeamsVisit", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Player> visitTeamPlayers;

    @ManyToOne
    @JoinColumn(name = "visit_team_id")
    private Team visitTeam;


    @ManyToOne
    @JoinColumn(name = "managerProposer_id")
    private Manager managerProposer;

    @ManyToOne
    @JoinColumn(name = "managerReceiver_id")
    private Manager managerReceiver;


    @EqualsAndHashCode.Exclude
    @Pattern(regexp = "[0-9]-[0-9]", message = " Wynik musi być w formacie liczba-liczba, (minusowe liczby nie są akceptowalne) ")
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


    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    private StatusOfMatch statusOfMatch;


    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate newDateOfGame;


}
