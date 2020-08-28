package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;

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
    private Integer weight;
    private Integer growth;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "player")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Contract> contracts = new LinkedHashSet<>();

}
