package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Message;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String repeatPassword;
    @Enumerated(EnumType.STRING)
    private Role role;


    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userSender",fetch = FetchType.LAZY)
    private Set<Message> messagesSend = new LinkedHashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userReceiver", fetch = FetchType.LAZY)
    private Set<Message> messagesGot = new LinkedHashSet<>();





}
