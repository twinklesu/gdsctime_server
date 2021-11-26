package seoultech.gdsc.web.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.dto.MessageDto;
import seoultech.gdsc.web.entity.Message;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.service.MessageService;

import java.util.List;

@SpringBootTest
@Transactional
public class MessageRepositoryTest extends WebApplicationTests {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void insertTest() {
        User user1 = userRepository.findById(1).get();
        User user2 = userRepository.findById(5).get();
        Message message = new Message();
        message.setFromUser(user1);
        message.setToUser(user2);
        message.setContent("1이 5에게 보내는 메세지 스프링에서");
        messageRepository.save(message);
    }

    @Test
    @Transactional
    public void findDetailMessageTest() throws JsonProcessingException {
        List<Message> messageList = messageRepository.findDetailMessage(1, 5);
        System.out.println(objectMapper.writeValueAsString(messageList));
    }
}
