package pjwstk.praca_inzynierska.symulatorligipilkarskiej;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
public class SymulatorligipilkarskiejApplication implements CommandLineRunner {


    @Autowired
    private UserRepository<User> userUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public SymulatorligipilkarskiejApplication() {

    }


    public static void main(String[] args) {


        SpringApplication.run(SymulatorligipilkarskiejApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {

    }
}
