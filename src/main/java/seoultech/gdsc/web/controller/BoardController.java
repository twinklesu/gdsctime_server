package seoultech.gdsc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seoultech.gdsc.web.dto.BoardDto;
import seoultech.gdsc.web.dto.CommentDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;
import seoultech.gdsc.web.service.BoardService;
import seoultech.gdsc.web.service.CommentService;

import javax.persistence.Basic;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;
    private final CommentService commentService;

    @Autowired
    public BoardController(BoardService boardService, HttpSession session, CommentService commentService) {
        this.boardService = boardService;
        this.session = session;
        this.commentService = commentService;
    }

    /*
    특정 카테고리의 전체 글 조회
     */
    @GetMapping("")
    public BasicResponse getBoard(@RequestParam int category) {
        List<BoardDto.Response> boardList = boardService.getCategoryList(category);
        return new SuccessResponse<>(boardList);
    }

    /*
    글 조회
     */
    @GetMapping("/detail/{id}")
    public BasicResponse getBoardDetail(@PathVariable int id) {
        BoardDto.DetailResponse board = boardService.getBoardDetail(id);
        return new SuccessResponse<>(board);
    }

    /*
    글 작성
     */
    @PostMapping("")
    public BasicResponse postBoard(@RequestBody BoardDto.Request newBoard) {
        int sessionId = (int) session.getAttribute("springSes");
        boardService.writeNew(newBoard, sessionId);
        return new SuccessResponse<>(new EmptyJsonResponse());
    }

    /*
    댓글 조회
     */
    @GetMapping("/{id}/comment")
    public BasicResponse getComment(@PathVariable int id) {
        List<CommentDto.Response> commentList = commentService.getComments(id);
        return new SuccessResponse<>(commentList);
    }

    /*
    댓글 작성
     */
    @PostMapping("/comment")
    public BasicResponse postComment(@RequestBody CommentDto.Request newComment) {
        int sessionId = (int) session.getAttribute("springSes");
        commentService.writeNew(newComment, sessionId);
        return new SuccessResponse<>(new EmptyJsonResponse());
    }

    //
    // 여기서 부터 BoardMain으로 나눠도 될 것 같기두.....
    //
    /*
    각 게시판의 최신 글 조회
     */
    @GetMapping("/main/myboard")
    public BasicResponse getMyBoard() {
        List<BoardDto.RecentResponse> res = boardService.getRecentBoard();
        return new SuccessResponse<>(res);
    }

    /*
    메인용 핫 게시판
     */
    @GetMapping("/main/hot")
    public BasicResponse getHotBoard() {
        List<BoardDto.HotResponse> res = boardService.getHotBoard();
        return new SuccessResponse<>(res);
    }

    /*
    실시간 게시판
     */

    /*
    카테고리별 핫 게시물
     */
    @GetMapping("/main/filter")
    public BasicResponse getMainFilter(@RequestParam(value="category") int category,
                                       @RequestParam(value="hot") int hot) {
        boolean isHot = hot != 0;
        List<BoardDto.HotResponse> res = boardService.getFilteredBoard(category, isHot);
        return new SuccessResponse<>(res);
    }

    /*
    게시판 별 검색
     */
    @PostMapping("/search")
    public BasicResponse postSearchCategory(@RequestParam("category") int category,
                                            @RequestParam("keyword") String keyword) {
        List<BoardDto.SearchResponse> res = boardService.searchCategory(category, keyword);
        return new SuccessResponse<>(res);
    }

}
