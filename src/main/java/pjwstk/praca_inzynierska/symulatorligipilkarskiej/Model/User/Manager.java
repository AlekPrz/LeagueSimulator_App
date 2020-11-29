package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.ManagerTeam;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ManagerId")
@Data

public class Manager extends User {


    @Pattern(regexp = "[A-Z][a-z]+", message = " Imie musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+", message = " Nazwisko musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String surname;
    private Integer age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ManagerTeam> managerTeams = new LinkedHashSet<>();





}
