package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoultech.gdsc.web.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
