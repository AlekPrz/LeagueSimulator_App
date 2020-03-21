package pjwstk.praca_inzynierska.symulatorligipilkarskiej;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SymulatorligipilkarskiejApplication {

    public static void main(String[] args) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        String encodedPass = passwordEncoder.encode("123");

        System.out.println(encodedPass);

        SpringApplication.run(SymulatorligipilkarskiejApplication.class, args);
    }

}
