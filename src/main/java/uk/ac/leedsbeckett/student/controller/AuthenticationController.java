package uk.ac.leedsbeckett.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.User;
import uk.ac.leedsbeckett.student.service.AuthenticationService;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        return authenticationService.showLoginError(model);
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        return authenticationService.showRegistrationForm(model);
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid User user) {
        return authenticationService.registerNewUser(user);
    }
}
