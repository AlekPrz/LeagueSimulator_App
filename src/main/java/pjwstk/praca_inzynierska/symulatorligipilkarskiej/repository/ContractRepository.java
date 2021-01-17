package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Long> {
    @Query("select distinct c.player from Contract c where c.team.id = :id")
    public List<Player> getAllPlayersIdsFromTeam(Long id);



}
