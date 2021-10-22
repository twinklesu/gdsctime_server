package seoultech.gdsc.web.dto;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.entity.Comment;
import seoultech.gdsc.web.repository.BoardRepository;
import seoultech.gdsc.web.repository.CommentRepository;
import seoultech.gdsc.web.repository.UserRepository;

@SpringBootTest
@Transactional
public class CommentDtoTest extends WebApplicationTests {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;


    @Test
    @Transactional
    public void RequestTest() throws JsonProcessingException {
        CommentDto.Request req = new CommentDto.Request();
        req.setBoardId(1);
        req.setContent("좋은 글 감사합니다");
        req.setIsSecret(true);
        Comment newComment = modelMapper.map(req, Comment.class);
        userRepository.findById(92).ifPresent(newComment::setUser);
        boardRepository.findById(req.getBoardId()).ifPresent(newComment::setBoard);
        System.out.println("####RequestTest#####");
        System.out.println(objectMapper.writeValueAsString(newComment));
    }
}
