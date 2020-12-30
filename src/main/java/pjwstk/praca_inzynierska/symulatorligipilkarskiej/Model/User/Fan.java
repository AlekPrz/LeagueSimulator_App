package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.LeagueOfFans;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "FanId")

public class Fan extends User {

    private String favouriteTeam;
    @ManyToOne
    @JoinColumn(name = "league_Of_Fans_id")
    private LeagueOfFans leagueOfFans;



}
