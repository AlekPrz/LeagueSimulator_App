package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.Data;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Season;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class SeasonTeam {

    @Id
    @GeneratedValue
    private Long id;
    private Integer points;
    private Integer goals;
    private boolean isCurrently;



    // DRUŻYNA BRALA UDZIAL W WIELU SEZONACH
    @ManyToOne
    @JoinColumn(name = "team_id")
     private Team team;
    @ManyToOne
    @JoinColumn(name = "season_id")
     private Season season;








}
