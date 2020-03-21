package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@Entity
public class Manager extends User {
    @Id
    @GeneratedValue
    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private Team team;



}
