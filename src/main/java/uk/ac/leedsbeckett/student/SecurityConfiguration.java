package uk.ac.leedsbeckett.student;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.leedsbeckett.student.repository.UserRepository;
import uk.ac.leedsbeckett.student.service.PortalService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final PortalService portalService;

    public SecurityConfiguration(DataSource dataSource, UserRepository userRepository, PortalService portalService) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.portalService = portalService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return portalService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/register/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll()
                .and()
                .formLogin().loginPage("/login")
                .usernameParameter("userName")
                .defaultSuccessUrl("/home", true)
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

}