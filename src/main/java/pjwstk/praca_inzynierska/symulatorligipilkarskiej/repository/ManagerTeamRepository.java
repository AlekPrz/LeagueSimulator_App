package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.ManagerTeam;


@Repository
public interface ManagerTeamRepository extends JpaRepository<ManagerTeam,Long> {
}
