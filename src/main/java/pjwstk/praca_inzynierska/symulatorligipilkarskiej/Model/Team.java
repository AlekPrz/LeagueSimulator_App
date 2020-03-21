package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;


import javax.persistence.*;

@Entity
public class Team {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String shortName;
    private String colors;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Manager manager;
}
