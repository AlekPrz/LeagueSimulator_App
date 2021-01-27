package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Fan;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.User;
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
        user.setRepeatPassword(passwordEncoder.encode(user.getRepeatPassword()));

        if (user.getRole().getDescription().equals("ROLE_FAN")) {
            userRepository.save(Fan.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .repeatPassword(user.getRepeatPassword())
                    .role(user.getRole())
                    .favouriteTeam("Do poprawy")
                    .build());
        } else if (user.getRole().getDescription().equals("ROLE_MANAGER")) {
            userRepository.save(Manager.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .repeatPassword(user.getRepeatPassword())
                    .role(user.getRole()).build());
        } else if(user.getRole().getDescription().equals("ROLE_ADMIN")){
            userRepository.save(user);

        }
    }

    public static String encodePassword(String s) {

        return passwordEncoder.encode(s);


    }
    }


