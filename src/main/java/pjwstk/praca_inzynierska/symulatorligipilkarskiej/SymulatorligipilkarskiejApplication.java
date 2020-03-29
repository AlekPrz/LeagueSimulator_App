package pjwstk.praca_inzynierska.symulatorligipilkarskiej;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Users.User;

@SpringBootApplication
public class SymulatorligipilkarskiejApplication {

    public static void main(String[] args) {


        SpringApplication.run(SymulatorligipilkarskiejApplication.class, args);
    }

}
