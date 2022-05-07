package uk.ac.leedsbeckett.student.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.User;
import uk.ac.leedsbeckett.student.service.AuthenticateService;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    private final AuthenticateService authenticateService;

    public AuthenticationController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        return authenticateService.showLoginError(model);
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        return authenticateService.showRegistrationForm(model);
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid User user) {
        return authenticateService.registerNewUser(user);
    }
}
