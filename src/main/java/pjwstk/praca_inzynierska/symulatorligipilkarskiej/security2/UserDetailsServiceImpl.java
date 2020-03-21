package pjwstk.praca_inzynierska.symulatorligipilkarskiej.security2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Role;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;


@Component
@Qualifier("myUserImpl")
public class UserDetailsServiceImpl implements UserDetailsService {



    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user =  userRepository.findByUsername(s).orElseThrow(()-> new RuntimeException("Not founded"));


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
               true, true, true, true,
                getAuthorities(user.getRole())
        );
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Arrays.asList(new SimpleGrantedAuthority(role.getDescription()));
    }
}
