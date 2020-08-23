package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "FanId")

public class Fan extends User {

    private String nameInTheLeagueOfFans;
    private String favouriteTeam;



}
