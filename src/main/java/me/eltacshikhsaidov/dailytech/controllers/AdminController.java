package me.eltacshikhsaidov.dailytech.controllers;

import me.eltacshikhsaidov.dailytech.repositories.BlogRepository;
import me.eltacshikhsaidov.dailytech.repositories.UserRepository;
import me.eltacshikhsaidov.dailytech.services.BlogService;
import me.eltacshikhsaidov.dailytech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private UserService userService;

    private BlogService blogService;

    @Autowired
    private UserRepository userRepository;

    private BlogRepository blogRepository;

    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/admin")
    public String adminPageUsers(Model model, @RequestParam(defaultValue = "") String name) {
        model.addAttribute("users", userService.findByName(name));
        model.addAttribute("blogs", blogRepository.findAll());
//        model.addAttribute("blogs", blogService.getBlogById(id));

        return "admin";
    }

//    @GetMapping("/admin")
//    public String adminPageBlogs(Model model, @RequestParam(defaultValue = "") String title) {
//        model.addAttribute("blogs", blogService.findAllBlogs());
//        return "views/adminPage";
//    }

    // --------- Deleting selected blog ---------------------------------

    @GetMapping("/users/delete/{email}")
    public String deleteProductById(@PathVariable("email") String email) {
        System.out.println(userService.isUserPresent(email));
//        userService.(email);
//        userService.isUserPresent(email);
//        userRepository.delete(email);
        return "redirect:/admin";
    }

    // ------------------------------------------------------------------
    // --------- Deleting selected blog ---------------------------------

    @GetMapping("/blogs/delete/{id}")
    public String deleteProductById(@PathVariable("id") Long id) {
        blogService.deleteBlogById(id);
        return "redirect:/admin";
    }

    // ------------------------------------------------------------

    //

}
