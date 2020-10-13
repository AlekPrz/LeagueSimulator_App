package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @Id
    @GeneratedValue
    @Column(name = "PlayerTeam_id")
    Long id;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startOfContract;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOfContract;
    private Boolean isCurrently;
    private Long goals;
    private BigDecimal salary;
}
