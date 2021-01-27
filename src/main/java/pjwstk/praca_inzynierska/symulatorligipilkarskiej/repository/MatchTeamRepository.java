package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.MatchTeam;

public interface MatchTeamRepository extends JpaRepository<MatchTeam, Long> {



    Page<MatchTeam> findAllByOrderByQueue(Pageable pageable);

}
