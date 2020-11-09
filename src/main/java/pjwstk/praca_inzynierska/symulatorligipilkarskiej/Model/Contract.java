package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

import lombok.*;
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
    Long id;
    @ManyToOne
    @JoinColumn(name = "team_id")
    @ToString.Exclude

    private Team team;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startOfContract;
    private Boolean isCurrently;
    private Long goals;
}
