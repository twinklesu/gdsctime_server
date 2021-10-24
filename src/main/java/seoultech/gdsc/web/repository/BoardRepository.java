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
    List<Board> findAllByIsHotOrderByCreatedAtDesc(@Param(value = "isHot") Boolean isHot);
    List<Board> findTop2ByIsHotOrderByCreatedAtDesc(@Param(value = "isHot") Boolean isHot);
    List<Board> findTop2ByIsHotAndBoardCategory_IdOrderByCreatedAtDesc(@Param(value = "isHot") Boolean isHot, @Param(value = "boardCategory") int BoardCategory);
    List<Board> findTop2ByBoardCategory_IdOrderByCreatedAtDesc(@Param(value = "boardCategory") int BoardCategory);
}
