package seoultech.gdsc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seoultech.gdsc.web.dto.BoardDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;
import seoultech.gdsc.web.service.BoardService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    @Autowired
    public BoardController(BoardService boardService, HttpSession session) {
        this.boardService = boardService;
        this.session = session;
    }

    /*
    특정 카테고리의 전체 글 조회
     */
    @GetMapping("/{category}")
    public BasicResponse getBoard(@PathVariable int category) {
        List<BoardDto.Response> boardList = boardService.getCategoryList(category);
        return new SuccessResponse<>(boardList);
    }

    /*
    글 조회
     */
    @GetMapping("/detail/{id}")
    public BasicResponse getBoardDetail(@PathVariable int id) {
        BoardDto.Response board = boardService.getBoardDetail(id);
        return new SuccessResponse<>(board);
    }

    /*
    글 작성
     */
    @PostMapping("")
    public BasicResponse postBoard(@RequestBody BoardDto.Request newBoard) {
        int sessionId = (int) session.getAttribute("springSes");
        BoardDto.Response res = boardService.writeNew(newBoard, sessionId);
        return new SuccessResponse<>(res);
    }
}
