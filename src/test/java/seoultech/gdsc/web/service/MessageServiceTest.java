package seoultech.gdsc.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.dto.MessageDto;

import java.util.List;

@SpringBootTest
@Transactional
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void getAllRecentMsgTest() throws JsonProcessingException {
        List<MessageDto.Response> res = messageService.getAllRecentMsg(1);
        System.out.println(objectMapper.writeValueAsString(res));
    }

    @Test
    @Transactional
    public void getDetailMsgTest() throws JsonProcessingException {
        List<MessageDto.Response> res = messageService.getDetailMsg(1, 5);
        System.out.println(objectMapper.writeValueAsString(res));
    }
}
