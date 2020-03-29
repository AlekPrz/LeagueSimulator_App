package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ManagerId")

public class Manager extends User {

    private String superName;
    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL)
    private Team team;




}
