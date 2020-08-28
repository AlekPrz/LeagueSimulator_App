package pjwstk.praca_inzynierska.symulatorligipilkarskiej.security2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.lang.model.element.QualifiedNameable;

@Configuration
public class MyWebSecurity extends WebSecurityConfigurerAdapter {


   private UserDetailsService userDetailsService;
   private PasswordEncoder passwordEncoder;

    public MyWebSecurity(@Qualifier("myUserImpl") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/*").permitAll()
                .antMatchers("/użytkownik/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/manager/**").hasRole("MANAGER")

                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/app-login")
                .defaultSuccessUrl("/default")
                .failureUrl("/login/error")
                .permitAll();


    }


}
