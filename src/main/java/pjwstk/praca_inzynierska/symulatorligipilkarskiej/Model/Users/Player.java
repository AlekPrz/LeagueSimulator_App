package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@PrimaryKeyJoinColumn(name = "ManagerId")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor


public class Player extends User {


    @Enumerated(EnumType.STRING)
    private Position position;
    private boolean hasTeam;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "player")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PlayerTeam> playerTeams = new LinkedHashSet<>();

}
