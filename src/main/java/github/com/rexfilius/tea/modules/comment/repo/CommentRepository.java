package github.com.rexfilius.tea.modules.comment.repo;

import github.com.rexfilius.tea.modules.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);
}
