package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Position;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "PlayerId")
@Data

public class Player extends User {

    @Pattern(regexp = "[A-Z][a-z]+", message = " Imie musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+", message = " Nazwisko musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String surname;
    @Enumerated(EnumType.STRING)
    private Position position;
    private String shirtName;
    @Min(value = 18, message = "Wiek musi być większy niż 16")
    private Integer age;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Contract> contracts = new LinkedHashSet<>();
}
