package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Season {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate seasonStart;
    private LocalDate seasonEnd;

    @OneToMany(mappedBy = "season", cascade = CascadeType.PERSIST)
    private Set<MatchTeam> match = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "season")
    private Set<SeasonTeam> seasonTeams = new LinkedHashSet<>();






}
