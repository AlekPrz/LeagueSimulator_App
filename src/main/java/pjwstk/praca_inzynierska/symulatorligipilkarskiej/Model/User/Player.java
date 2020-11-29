package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "PlayerId")
@Data

public class Player extends User {

    @Pattern(regexp = "[A-Z][a-z]+", message = " Imie musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+", message = " Nazwisko musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String surname;
    @Enumerated(EnumType.STRING)
    private Position position;
    private String shirtName;

    @Min(value = 16, message = "Wiek musi być większy niż 16")
    private Integer age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Contract> contracts = new LinkedHashSet<>();


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "MatchTeamHomePlayers",
            joinColumns = {@JoinColumn(name = "player_id")},
            inverseJoinColumns = {@JoinColumn(name = "matchTeam_id")}
    )
    Set<MatchTeam> matchTeamsHome = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "MatchTeamVisitPlayers",
            joinColumns = {@JoinColumn(name = "player_id")},
            inverseJoinColumns = {@JoinColumn(name = "matchTeam_id")}
    )
    Set<MatchTeam> matchTeamsVisit = new LinkedHashSet<>();


}
