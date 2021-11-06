package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    @Query("select board from Board board where board.id in (select max(board.id) from board group by board.boardCategory) and board.boardCategory.id <= 6 order by board.boardCategory.id asc")
    List<Board> findRecentBoard();

    @Query(value="select *, like_num+comment_num as total from board where is_hot = 1 and created_at > DATE_ADD(now(), INTERVAL -24 HOUR) order by total DESC, created_at DESC LIMIT 2", nativeQuery = true)
    List<Board> findRealtime();

    @Query("select board from Board board where board.content like %?1% or board.title like %?2%")
    List<Board> findAllByKeyword(String contentKeyword, String titleKeyword);

    @Query("select board from Board board where (board.content like %?1% or board.title like %?2%) and board.boardCategory.id = ?3")
    List<Board> findAllByCategoryByKeyword(String contentKeyword, String boardKeyword, int categoryId);


}
