package seoultech.gdsc.web.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserDtoTest extends WebApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Transactional
    public void RequestTest() throws JsonProcessingException {
        UserDto.Request req = new UserDto.Request();
        req.setUserId("twinklesu");
        req.setPassword("0104");
        req.setEmail("twinklesu914@gamil.com");
        req.setName("박수빈");
        req.setNickname("숩니");
        req.setMajor("ITM");
        User user = modelMapper.map(req, User.class);
        System.out.println(objectMapper.writeValueAsString(user));
    }
}
