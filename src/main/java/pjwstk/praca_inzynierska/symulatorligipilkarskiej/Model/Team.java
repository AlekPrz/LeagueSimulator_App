package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users.PlayerTeam;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "team_id")

    private Long id;
    private String name;
    private String shortName;
    private String colors;
    private boolean isInLeague;
    @OneToOne
    @JoinColumn(name = "managerId")
    private Manager manager;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<PlayerTeam> playerTeams = new LinkedHashSet<>();

}
