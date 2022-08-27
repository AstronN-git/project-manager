package com.max.projectmanager.controller.security;

import com.max.projectmanager.entity.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {

    private final UserDetailsManager userDetailsManager;

    public SignupController(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

//    @PostMapping("/signup/confirm")
//    public String confirmSignup(@ModelAttribute User user) {
//        user.setActive(true);
//
//    }
}
