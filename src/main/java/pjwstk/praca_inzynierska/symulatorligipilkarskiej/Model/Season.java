package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.MatchTeam;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Season {

    @Id
    @GeneratedValue
    private Long id;
    private String seasonName;

    @OneToMany(mappedBy = "season", cascade = CascadeType.PERSIST)
    private Set<MatchTeam> match = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "season")
    private Set<SeasonTeam> seasonTeams = new LinkedHashSet<>();






}
