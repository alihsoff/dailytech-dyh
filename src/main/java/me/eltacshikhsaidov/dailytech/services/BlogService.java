package me.eltacshikhsaidov.dailytech.services;

import me.eltacshikhsaidov.dailytech.entities.Blog;
import me.eltacshikhsaidov.dailytech.entities.User;
import me.eltacshikhsaidov.dailytech.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BlogService {

    private BlogRepository blogRepository;

    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public void addBlog(Blog blog, User user) {
        blog.setUser(user);
        //blog.setDate(LocalDateTime.now());
        blogRepository.save(blog);
    }

    public void editBlog(Blog blog) {
        Blog newBlog = blogRepository.findOne(blog.getId());
        newBlog.setDescription(blog.getDescription());
        newBlog.setTitle(blog.getTitle());
        blogRepository.save(newBlog);
    }


    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    public List<Blog> findUserBlog(User user) {
        return blogRepository.findByUser(user);
    }


    // -------- Adding search functionality for blogs ---------------

    public List<Blog> findByTitle(String title) {
        return blogRepository.findByTitleLike("%" + title + "%");
    }

    //----------------------------------------------------------------

    // ---------- Adding read more page functionality -----------------



    // ----------------------------------------------------------------

    // ----------------- Beautify HTML using Java ----------------------

    public String beautifyHTML(Blog blog) {
        String description = blog.getDescription();
        return description.replaceAll("<", "\n<");

    }

    // -----------------------------------------------------------------

    // ---------- Find all blogs ----------------------

    public List<Blog> findAllBlogs() {
        return blogRepository.findAll();
    }

    // ------------------------------------------------


    // ------ Adding functionality for deleting Blogs ------------------

    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }

    // -----------------------------------------------------------------

}
