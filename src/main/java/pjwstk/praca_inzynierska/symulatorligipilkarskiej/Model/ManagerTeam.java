package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerTeam {
    @Id
    @GeneratedValue
    @Column(name = "ManagerTeam_Id")
    Long id;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startOfContract;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOfContract;

    private Boolean isCurrently;
}
