package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Fan;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @Id
    @GeneratedValue
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate seasonStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate seasonEnd;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private Set<MatchTeam> match = new LinkedHashSet<>();

    public void addMatchTeam(MatchTeam matchTeam) {
        match.add(matchTeam);
        matchTeam.setSeason(this);
    }

    public void removeMatchTeam(MatchTeam matchTeam) {
        match.remove(matchTeam);
        matchTeam.setSeason(null);
    }


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "season")
    private Set<SeasonTeam> seasonTeams = new LinkedHashSet<>();




}
