package pjwstk.praca_inzynierska.symulatorligipilkarskiej;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
public class SymulatorligipilkarskiejApplication {


    private static TeamRepository teamRepository;
    private static MatchTeamRepository matchTeamRepository;
    private static SeasonRepository seasonRepository;
    private static ContractRepository contractRepository;
    private static SeasonTeamRepository seasonTeamRepository;

    public SymulatorligipilkarskiejApplication(TeamRepository teamRepository,
                                               MatchTeamRepository matchTeamRepository,
                                               SeasonRepository seasonRepository, ContractRepository contractRepository,
                                               SeasonTeamRepository seasonTeamRepository) {
        this.teamRepository = teamRepository;
        this.matchTeamRepository = matchTeamRepository;
        this.seasonRepository = seasonRepository;
        this.contractRepository = contractRepository;
        this.seasonTeamRepository = seasonTeamRepository;
    }


    public static void main(String[] args) {


        SpringApplication.run(SymulatorligipilkarskiejApplication.class, args);




    }
}



