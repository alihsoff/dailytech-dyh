package me.eltacshikhsaidov.dailytech.controllers;

import me.eltacshikhsaidov.dailytech.dto.Contact;
import me.eltacshikhsaidov.dailytech.entities.Blog;
import me.eltacshikhsaidov.dailytech.entities.User;
import me.eltacshikhsaidov.dailytech.services.BlogService;
import me.eltacshikhsaidov.dailytech.services.MailService;
import me.eltacshikhsaidov.dailytech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;

@Controller
public class IndexController {

    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private BlogService blogService;

    private MailService mailService;

    private UserService userService;


    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

//    @GetMapping("/")
//    public String showIndexPage() {
//        return "index";
//    }

    @GetMapping("/about")
    public String showAboutPage(Model model) {
        model.addAttribute("current_year", CURRENT_YEAR);
        return "about";
    }

    @GetMapping("/contact")
    public String showContactPage(Contact contact, Model model) {
        model.addAttribute("current_year", CURRENT_YEAR);
        return "contact";
    }

    @PostMapping("/contact")
    public String sendMail(Contact contact, Model model) {
        mailService.processMailRequest(contact);
        model.addAttribute("current_year", CURRENT_YEAR);
        model.addAttribute("success", "Mesajınız göndərildi!!!");
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // -------------- In case if you don't remember the password ----------------

    @GetMapping("/forget")
    public String forgetPassword() {
        return "forget";
    }

    // --------------------------------------------------------------------------


    // ------------- Adding search functionality -------------------------

    @GetMapping("/")
    public String searchBlogs(Model model, @RequestParam(defaultValue = "") String title) {
        model.addAttribute("blogs", blogService.findByTitle(title));
        model.addAttribute("current_year", CURRENT_YEAR);
        return "index";
    }

    // --------------- Reading whole blog in new page ---------------------

//    @GetMapping("/readMore/{id}")
//    public String readWholeBlog(Model model, @PathVariable Long id) {
//
//        Blog selectedBlog = blogService.getBlogById(id);
//
//        String text = selectedBlog.getDescription();
//
//        model.addAttribute("selectedBlog", text);
//        return "readBlog";
//
//    }

    @GetMapping("/more/{id}")
    public String readWholeBlog(Model model, @PathVariable Long id) {
        Blog seletedBlog = blogService.getBlogById(id);
        model.addAttribute("selectedBlog", seletedBlog);
        model.addAttribute("current_year", CURRENT_YEAR);
        return "more";
    }

    // ----------------------------------------------------------------------

    // ------------------- Creating Page for Posting blogs ------------------

    @GetMapping("/createBlog")
    public String blogForm(Model model, HttpSession httpSession, String email) {
        httpSession.setAttribute("email", email);
        model.addAttribute("blog", new Blog());
        return "views/blogForm";
    }

    @PostMapping("/createBlog")
    public String addBlog(@Valid Blog blog, HttpSession httpSession, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "views/blogForm";
        }

        String email = (String) httpSession.getAttribute("email");
        blogService.addBlog(blog, userService.findOne(email));

        return "views/successBlog";
    }

    // ----------------------------------------------------------------------



}
