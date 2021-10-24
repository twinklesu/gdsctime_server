package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.Board;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAllByBoardCategory_Id(@Param(value = "categoryId") int categoryId);
    List<Board> findAllByUser_Id(@Param(value = "userId") int userId);
}
