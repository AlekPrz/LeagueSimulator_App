package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;

import javax.persistence.Id;
import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Id> {
    Optional<User> findUserByUsername(String username);
    Optional<Manager> findManagerByUsername(String username);

}
