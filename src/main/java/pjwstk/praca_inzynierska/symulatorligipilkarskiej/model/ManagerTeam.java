package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;

import javax.persistence.*;
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startOfContract;

    private Boolean isCurrently;
}
