package com.max.projectmanager.controller.security;

import com.max.projectmanager.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    private final UserDetailsManager userDetailsManager;

    @Value("${url.security.login}")
    private String loginUrl;

    public SignupController(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup/confirm")
    public String confirmSignup(@ModelAttribute User user) {
        user.setActive(true);
        userDetailsManager.createUser(user);
        return "redirect:" + loginUrl;
    }
}
