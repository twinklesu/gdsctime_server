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
        BoardDto.Response res = boardService.getBoardDetail(2);
        System.out.println(objectMapper.writeValueAsString(res));
        res = boardService.getBoardDetail(3);
        System.out.println(objectMapper.writeValueAsString(res));
    }

    @Test
    @Transactional
    public void writeNewTest() throws JsonProcessingException {
        System.out.println("##########writeNewTest##########");
        BoardDto.Request req = new BoardDto.Request();
        req.setCategory(1);
        req.setTitle("service로 글 작성하기");
        req.setContent("성공 가즈아아아아아ㅏ아");
        req.setIsSecret(true);
        System.out.println("request: " + objectMapper.writeValueAsString(req));
        BoardDto.Response res = boardService.writeNew(req, 92);
        System.out.println("response: " + objectMapper.writeValueAsString(res));
    }
}