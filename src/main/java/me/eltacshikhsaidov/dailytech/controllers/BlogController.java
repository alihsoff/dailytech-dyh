package me.eltacshikhsaidov.dailytech.controllers;

import me.eltacshikhsaidov.dailytech.entities.Blog;
import me.eltacshikhsaidov.dailytech.services.BlogService;
import me.eltacshikhsaidov.dailytech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class BlogController {

    private static final String UPLOAD_DIR =  System.getProperty("user.dir")+"/images/";

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

    @GetMapping("/addBlog")
    public String blogForm(String email , Model model , HttpSession httpSession) {

        httpSession.setAttribute("email", email);
        System.out.println(email);
        model.addAttribute("blog", new Blog());

        return "newblog";
    }

    @GetMapping("/editBlog/{id}")
    public String editBlog(Model model , @PathVariable("id") Long blogId) {

        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Blog blog = blogService.getBlogById(blogId);
        if(email.equals(blog.getUser().getEmail())) {
            model.addAttribute("blog", blog);
        }


        return "editblog";
    }

    @PostMapping("/editBlog")
    public String editBlog(Blog blog) {

        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        System.out.println(blog);

            blogService.editBlog(blog);

        return "redirect:/profile";
    }

    @PostMapping("/deleteBlog/{id}")
    public String editBlog(@PathVariable("id") Long blogId) {

        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Blog blog = blogService.getBlogById(blogId);
        if(email.equals(blog.getUser().getEmail()))
            blogService.deleteBlogById(blogId);

        return "redirect:/profile";
    }

    @PostMapping("/addBlog")
    public String addBlog(@ModelAttribute Blog blog, BindingResult bindingResult,
                          @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "redirect:/index";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            System.out.println(path);
            blog.setImgPath(fileName);
            blogService.addBlog(blog, userService.findOne(email));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile";
    }

}
