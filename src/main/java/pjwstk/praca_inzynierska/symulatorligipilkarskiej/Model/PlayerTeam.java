package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity

public class PlayerTeam {

    @Id
    @GeneratedValue
    @Column(name = "PlayerTeam_id")
    Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "player_id")
    private Player player;
    private LocalDate startOfContract;
     //Jeśli data kontraktu jest późniejsza niż data to aktualny
    private LocalDate endOfContract;
    private Long goals;
    private BigDecimal salary;
}
