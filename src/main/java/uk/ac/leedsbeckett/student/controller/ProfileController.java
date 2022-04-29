package uk.ac.leedsbeckett.student.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.Student;
import uk.ac.leedsbeckett.student.model.User;
import uk.ac.leedsbeckett.student.service.ProfileService;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping("/profile")
    public ModelAndView profile(@RequestAttribute("user") User user, @Nullable @RequestAttribute("student") Student student) {
        return profileService.getProfile(user, student, "profile");
    }

    @GetMapping("/editProfile/{id}")
    public ModelAndView editProfile(@PathVariable Long id) {
        return profileService.getProfileToEdit(id);
    }

    @PostMapping("/editProfile")
    public ModelAndView editProfile(Student student) {
        return profileService.editProfile(student);
    }

}
