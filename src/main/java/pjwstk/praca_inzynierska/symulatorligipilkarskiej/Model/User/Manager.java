package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Season;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ManagerId")
@Data

public class Manager extends User {


    @Pattern(regexp = "[A-Z][a-z]+", message = " Imie musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+", message = " Nazwisko musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String surname;
    private Integer age;


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ManagerTeam> managerTeams = new LinkedHashSet<>();


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "managerProposer", fetch = FetchType.LAZY)
    private Set<MatchTeam> matchTeamProposer = new LinkedHashSet<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "managerReceiver", fetch = FetchType.LAZY)
    private Set<MatchTeam> matchTeamReceiver = new LinkedHashSet<>();


}
