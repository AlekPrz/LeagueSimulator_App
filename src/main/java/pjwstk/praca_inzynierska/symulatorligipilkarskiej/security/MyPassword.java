package pjwstk.praca_inzynierska.symulatorligipilkarskiej.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class MyPassword extends BCryptPasswordEncoder {
}
