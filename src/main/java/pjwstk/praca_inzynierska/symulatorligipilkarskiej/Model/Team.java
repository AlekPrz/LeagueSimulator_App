package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
public class Team {


    @Id
    @GeneratedValue
    private Long id;
    private String name;


     @Pattern(regexp = "[A-Z ]+", message = "SHORT_NAME IS NOT CORRECT")
     private String shortName;
     private String colors;




    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private Set<Contract> contracts = new LinkedHashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<ManagerTeam> managerTeams = new LinkedHashSet<>();



    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "homeTeam")
    private Set<MatchTeam> homeGames = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "visitTeam")
    private Set<MatchTeam> visitGames = new LinkedHashSet<>();


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<SeasonTeam> seasonTeams = new LinkedHashSet<>();



}
