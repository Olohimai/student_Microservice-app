package uk.ac.leedsbeckett.student.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.*;
import uk.ac.leedsbeckett.student.repository.StudentRepository;
import uk.ac.leedsbeckett.student.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class StudentInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final StudentRepository studentRepository;
    private Student student;
    private User user;

    public StudentInterceptor(UserService userService, StudentRepository studentRepository) {
        this.userService = userService;
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        findUserAndStudent();
        request.setAttribute("user", user);
        request.setAttribute("student", student);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        findUserAndStudent();
        request.setAttribute("isStudent", student != null);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    private void findUserAndStudent() {
        user = userService.getLoggedInUser();
        if (user != null) {
            student = studentRepository.findByUserId(user.getId());
        }
    }
}