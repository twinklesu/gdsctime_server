package seoultech.gdsc.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.MessageDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.Message;
import seoultech.gdsc.web.repository.MessageRepository;
import seoultech.gdsc.web.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;

    @Autowired
    public MessageService(UserRepository userRepository,
                          MessageRepository messageRepository,
                          EntityManager entityManager,
                          ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
    }

    /*
    사용자가 보내거나 받은 메세지 세트들의 최신 메세지만 return
     */
    public List<MessageDto.Response> getAllRecentMsg(int id) {
        String sql = "select * from message where id in " +
                        "(select id from (select least(from_user_id, to_user_id) as user1, greatest(from_user_id, to_user_id) as user2, max(id) as id " +
                        "from message where from_user_id = " + id  + " or to_user_id = " + id + " group by user1, user2) as subtable) " +
                        "order by created_at desc";
        Query query = entityManager.createNativeQuery(sql, Message.class);
        List<Message> messages = query.getResultList();
        List<MessageDto.Response> res = messages.stream().map(message -> {
            MessageDto.Response msgDto = modelMapper.map(message, MessageDto.Response.class);
            msgDto.setCreatedAt(message.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMddhhmm")));
            msgDto.setIsMine(message.getFromUser().getId() == id); // 보낸사람이 현재 사용자면 true, 아니면 false
            return msgDto;
        }).collect(Collectors.toList());
        return res;
    }

    /*
    특정 상대방과의 모든 메세지 기록
     */
    public List<MessageDto.Response> getDetailMsg(int userId, int otherId) {
        String param = userId + ", " + otherId;
        String jpql = "select m from Message m where m.toUser in (" + param + ") and m.fromUser in (" + param + ") order by m.createdAt desc";
        Query query = entityManager.createQuery(jpql);
        List<Message> messages = query.getResultList();
        List<MessageDto.Response> res = messages.stream().map(message -> {
            MessageDto.Response msgDto = modelMapper.map(message, MessageDto.Response.class);
            msgDto.setCreatedAt(message.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMddhhmm")));
            msgDto.setIsMine(message.getFromUser().getId() == userId); // 보낸사람이 현재 사용자면 true, 아니면 false
            return msgDto;
        }).collect(Collectors.toList());
        return res;
    }



}
