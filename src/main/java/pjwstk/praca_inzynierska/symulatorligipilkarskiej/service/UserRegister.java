package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

@Service

public class UserRegister {


    private UserRepository<User> userRepository;
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

    public void registerNewManager(User user) {


        if (user.getRole().getDescription().equals("ROLE_MANAGER")) {


            Manager manager = Manager.builder().username(user.getUsername()).password(UserRegister.encodePassword(user.getPassword()))
                    .repeatPassword(UserRegister.encodePassword(user.getRepeatPassword())).role(user.getRole()).build();

            userRepository.save(manager);

        }
        registerNewUser(user);


    }
}
