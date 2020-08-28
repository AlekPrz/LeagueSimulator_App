package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ManagerId")

public class Manager extends User {

    private Integer yearsOfExperience;


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ManagerTeam> managerTeams = new LinkedHashSet<>();





}
