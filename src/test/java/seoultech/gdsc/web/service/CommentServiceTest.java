package seoultech.gdsc.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.dto.CommentDto;
import seoultech.gdsc.web.dto.CommentDtoTest;

import java.util.List;

@SpringBootTest
@Transactional
public class CommentServiceTest extends WebApplicationTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void writeNewTest() throws JsonProcessingException {
        CommentDto.Request req = new CommentDto.Request();
        req.setBoardId(1);
        req.setIsSecret(true);
        req.setContent("좋은 글 감사합니다.");
        commentService.writeNew(req, 1);
        System.out.println("req: " + objectMapper.writeValueAsString(req));
    }

    @Test
    @Transactional
    public void getCommentsTest() throws JsonProcessingException {
        List<CommentDto.Response> res = commentService.getComments(1);
        System.out.println("res: " + objectMapper.writeValueAsString(res));
    }
}
