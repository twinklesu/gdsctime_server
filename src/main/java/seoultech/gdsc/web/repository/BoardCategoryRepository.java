package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoultech.gdsc.web.entity.BoardCategory;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Integer> {
}
