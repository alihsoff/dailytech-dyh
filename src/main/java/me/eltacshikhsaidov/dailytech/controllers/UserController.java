package me.eltacshikhsaidov.dailytech.controllers;

import me.eltacshikhsaidov.dailytech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam(defaultValue = "") String name) {
        model.addAttribute("users", userService.findByName(name));
        return "views/list";
    }

}
