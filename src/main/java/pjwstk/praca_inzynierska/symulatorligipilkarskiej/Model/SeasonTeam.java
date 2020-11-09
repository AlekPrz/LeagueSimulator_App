package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Season;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonTeam {

    @Id
    @GeneratedValue
    private Long id;
    private Integer points;
    private Integer goals;
    private Integer matchesDone;
    private Integer currentlyPlace;
    private boolean isCurrently;


    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;


}
