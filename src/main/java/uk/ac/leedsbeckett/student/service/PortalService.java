package uk.ac.leedsbeckett.student.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.*;
import uk.ac.leedsbeckett.student.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
@Component
public class PortalService implements UserDetailsService {

    private final UserRepository userRepository;

    public PortalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return new PortalUserDetails(user);
    }

    public ModelAndView loadPortalUserDetails(User user, Student student, @NotNull @NotEmpty String view) {
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("user", user);
        if (student != null) {
            modelAndView.addObject("student", student);
        }
        modelAndView.addObject("showFirstName", student != null && student.getForename() != null);
        return modelAndView;
    }

}