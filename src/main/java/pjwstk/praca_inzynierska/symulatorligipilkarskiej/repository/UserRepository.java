package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.User;

import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<Manager> findManagerByUsername(String username);
    Optional<Player> findByShirtName(String name);

}
