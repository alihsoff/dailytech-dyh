package main.dailytech.controllers;

import main.dailytech.entities.Blog;
import main.dailytech.services.AmazonS3ClientServiceImpl;
import main.dailytech.services.BlogService;
import main.dailytech.services.UserService;
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

@Controller
public class BlogController {

    //private static final String UPLOAD_DIR =  System.getProperty("user.dir")+"/images/";

    private AmazonS3ClientServiceImpl amazonS3ClientService;


    private BlogService blogService;

    private UserService userService;

    @Autowired
    public void setAmazonS3ClientService(AmazonS3ClientServiceImpl amazonS3ClientService) {
        this.amazonS3ClientService = amazonS3ClientService;
    }

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

    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") Long blogId) {

        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Blog blog = blogService.getBlogById(blogId);
        if(email.equals(blog.getUser().getEmail())) {
            blogService.deleteBlogById(blogId);
            if(blog.getImgPath()!=null){
                amazonS3ClientService.deleteFileFromS3Bucket(blog.getImgPath());
            }
        }

        return "redirect:/profile";
    }

    @PostMapping("/addBlog")
    public String addBlog(@ModelAttribute Blog blog, BindingResult bindingResult,
                          @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "redirect:/index";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        if(fileName!=null) {
            amazonS3ClientService.uploadFileToS3Bucket(file, true);
            blog.setImgPath(fileName);
        }
            blogService.addBlog(blog, userService.findOne(email));

        return "redirect:/profile";
    }

}
