package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
