package seoultech.gdsc.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.dto.BoardDto;

import java.util.List;

@SpringBootTest
@Transactional
public class BoardServiceTest extends WebApplicationTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void getCategoryListTest() throws JsonProcessingException {
        System.out.println("##########BoardServiceTest: getCategoryListTest#########");
        List<BoardDto.Response> res = boardService.getCategoryList(1);
        System.out.println(objectMapper.writeValueAsString(res));
    }

    @Test
    @Transactional
    public void getBoardDetailTest() throws JsonProcessingException {
        System.out.println("###########getBoardDetailTest#############");
        BoardDto.DetailResponse res = boardService.getBoardDetail(2);
        System.out.println(objectMapper.writeValueAsString(res));
        res = boardService.getBoardDetail(3);
        System.out.println(objectMapper.writeValueAsString(res));
    }

    @Test
    @Transactional
    public void writeNewTest() throws JsonProcessingException {
        System.out.println("##########writeNewTest##########");
        BoardDto.Request req = new BoardDto.Request();
        req.setCategoryId(1);
        req.setTitle("service로 글 작성하기");
        req.setContent("성공 가즈아아아아아ㅏ아");
        req.setIsSecret(true);
        System.out.println("request: " + objectMapper.writeValueAsString(req));
        boardService.writeNew(req, 1);
    }

    @Test
    @Transactional
    public void getRecentBoardTest() throws JsonProcessingException {
        System.out.println("#####getRecentBaordTest#######");
        List<BoardDto.RecentResponse> res = boardService.getRecentBoard();
        System.out.println(objectMapper.writeValueAsString(res));
    }


    @Test
    @Transactional
    public void searchCategoryTest() throws JsonProcessingException {
        System.out.println("#######searchCategoryTest########");
        List<BoardDto.SearchResponse> res = boardService.searchCategory(0,"내용");
        System.out.println(objectMapper.writeValueAsString(res));
    }

    @Test
    @Transactional
    public void getHotBoard() throws JsonProcessingException {
        List<BoardDto.HotResponse> res = boardService.getHotBoard();
        System.out.println(objectMapper.writeValueAsString(res));
    }

    @Test
    @Transactional
    public void getRealtime() throws JsonProcessingException {
        List<BoardDto.DetailResponse> res = boardService.getRealtime();
        System.out.println(objectMapper.writeValueAsString(res));
    }
}
