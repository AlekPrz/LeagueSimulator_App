package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Service

public class UserRegister {


    private UserRepository userRepository;
    private static PasswordEncoder passwordEncoder;


    public UserRegister(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(User user) {


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public static String encodePassword(String s) {

        return passwordEncoder.encode(s);


    }

}
