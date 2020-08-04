package me.eltacshikhsaidov.dailytech.controllers;

import me.eltacshikhsaidov.dailytech.entities.User;
import me.eltacshikhsaidov.dailytech.services.BlogService;
import me.eltacshikhsaidov.dailytech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    private BlogService blogService;

    private UserService userService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {

        String email = principal.getName();
        User user = userService.findOne(email);

        model.addAttribute("user", userService.findOne(email));

        model.addAttribute("blogs", blogService.findUserBlog(user));

        return "myblog";

    }

}
