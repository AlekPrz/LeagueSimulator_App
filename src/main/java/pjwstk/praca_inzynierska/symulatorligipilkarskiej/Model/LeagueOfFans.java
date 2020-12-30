package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Fan;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueOfFans {

    @Id
    @GeneratedValue
    Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "leagueOfFans")
    private Set<Fan> fans = new LinkedHashSet<>();


    @OneToOne(mappedBy = "leagueOfFans", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private Season season;
}
