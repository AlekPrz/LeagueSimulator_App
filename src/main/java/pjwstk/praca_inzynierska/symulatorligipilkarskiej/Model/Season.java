package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.SeasonTeam;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Season {

    @Id
    @GeneratedValue
    private Long id;
    private final static String currentlySeason = "19/20";
    //format "19/20"
    private String seasonName;

    @OneToMany(mappedBy = "season", cascade = CascadeType.PERSIST)
    private Set<MatchTeam> match;


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "currentlySeason")
    private Set<Team> seasonTeams =  new LinkedHashSet<>();





}
