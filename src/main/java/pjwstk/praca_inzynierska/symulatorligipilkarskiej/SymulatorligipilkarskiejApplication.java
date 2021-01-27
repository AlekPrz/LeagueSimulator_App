package pjwstk.praca_inzynierska.symulatorligipilkarskiej;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

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

        User user = userUserRepository.findById(1L).orElse(null);
    }
}
