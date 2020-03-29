package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users;

import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity

public class PlayerTeam {

    @Id
    Long id;
    @MapsId
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;
    @MapsId
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "player_id")
    private Player player;
    private LocalDate date;
}
