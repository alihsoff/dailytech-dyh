package me.eltacshikhsaidov.dailytech.repositories;

import me.eltacshikhsaidov.dailytech.entities.Blog;
import me.eltacshikhsaidov.dailytech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByUser(User user);

    List<Blog> findByTitleLike(String title);

    void deleteById(Long id);
}
