package main.dailytech.repositories;

import main.dailytech.entities.Blog;
import main.dailytech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByUser(User user);

    List<Blog> findByTitleLike(String title);

    void deleteById(Long id);
}
