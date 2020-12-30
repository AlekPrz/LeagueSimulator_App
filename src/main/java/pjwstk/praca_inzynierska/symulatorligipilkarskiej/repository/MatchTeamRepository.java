package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;

import javax.persistence.Id;
import java.util.List;

public interface MatchTeamRepository extends JpaRepository<MatchTeam, Long> {



    Page<MatchTeam> findAllByOrderByQueue(Pageable pageable);

}
