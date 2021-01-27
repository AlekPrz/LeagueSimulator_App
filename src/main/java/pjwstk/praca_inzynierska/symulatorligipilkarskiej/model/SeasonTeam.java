package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model;


import lombok.*;

import javax.persistence.*;

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
