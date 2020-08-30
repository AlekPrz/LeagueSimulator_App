package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

import lombok.Data;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class ManagerTeam {
    @Id
    @GeneratedValue
    @Column(name = "ManagerTeam_Id")
    Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "manager_id")
    private Manager manager;
    private LocalDate startOfContract;

    //Jeśli data kontraktu jest późniejsza niż data to aktualny
    private LocalDate endOfContract;

    private Boolean isCurrently;
}
