package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

    @Query(value = "select * from Team e where e.name like %:keyword% ", nativeQuery = true)
    List<Team> findByKeyword(@Param("keyword") String keyword);

}
