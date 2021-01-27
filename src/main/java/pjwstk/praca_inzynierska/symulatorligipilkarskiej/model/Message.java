package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.User;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Message {

    @Id
    @GeneratedValue
    Long id;
    private String subject;
    @ManyToOne
    @JoinColumn(name = "userSender_Id")
    private User userSender;
    @ManyToOne
    @JoinColumn(name = "userReceiver_Id")
    private User userReceiver;
    private String text;
    private LocalDate dateOfSend;

    private Boolean isDeletedBySender;
    private Boolean isDeleteByReceiver;
    private Boolean isRead;


}
