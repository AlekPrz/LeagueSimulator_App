package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "FanId")


public class Fan extends User {

    private String favouriteTeam;



}
