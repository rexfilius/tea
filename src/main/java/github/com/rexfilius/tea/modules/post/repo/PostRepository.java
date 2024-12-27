package github.com.rexfilius.tea.modules.post.repo;

import github.com.rexfilius.tea.modules.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryId(Long categoryId);
}