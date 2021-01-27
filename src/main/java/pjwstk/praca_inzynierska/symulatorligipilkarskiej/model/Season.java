package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

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
