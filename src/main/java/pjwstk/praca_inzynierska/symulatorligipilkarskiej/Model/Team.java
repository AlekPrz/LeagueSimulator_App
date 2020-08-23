package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.Data;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.SeasonTeam;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Team {


    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String shortName;
    private String colors;


    @ManyToOne
    @JoinColumn(name = "currentlySeason_id")
    private Season currentlySeason;


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<PlayerTeam> playerTeams = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<ManagerTeam> managerTeams = new LinkedHashSet<>();


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "homeTeam")
    private Set<MatchTeam> homeGames = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "visitTeam")
    private Set<MatchTeam> visitGames = new LinkedHashSet<>();


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<SeasonTeam> seasonTeams = new LinkedHashSet<>();



}
