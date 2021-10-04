package seoultech.gdsc.web.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.repository.UserRepository;

@SpringBootTest
@Transactional
public class BoardDtoTest extends WebApplicationTests {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void RequestTest() throws JsonProcessingException {
        BoardDto.Request req = new BoardDto.Request();
        req.setCategoryId(1);
        req.setTitle("service로 글 작성하기");
        req.setContent("성공 가즈아아아아아ㅏ아");
        req.setIsSecret(true);
        Board newBoard = modelMapper.map(req, Board.class);
        userRepository.findById(92).ifPresent(newBoard::setUser);
        System.out.println("#######RequestTest#############");
        System.out.println(objectMapper.writeValueAsString(newBoard));
    }
}
