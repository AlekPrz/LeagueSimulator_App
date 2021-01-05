package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Message;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByOrderByDateOfSendDesc();
}
