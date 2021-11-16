package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.MessageDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.Comment;
import seoultech.gdsc.web.entity.Message;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.BoardRepository;
import seoultech.gdsc.web.repository.CommentRepository;
import seoultech.gdsc.web.repository.MessageRepository;
import seoultech.gdsc.web.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public MessageService(UserRepository userRepository,
                          MessageRepository messageRepository,
                          EntityManager entityManager,
                          ModelMapper modelMapper,
                          CommentRepository commentRepository,
                          BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    /*
    사용자가 보내거나 받은 메세지 세트들의 최신 메세지만 return
     */
    public List<MessageDto.Response> getAllRecentMsg(int id) {
        List<Message> messages = messageRepository.findAllRecent(id, id);
        List<MessageDto.Response> res = messages.stream().map(message -> {
            MessageDto.Response msgDto = modelMapper.map(message, MessageDto.Response.class);
            msgDto.setMessageId(message.getId());
            msgDto.setCreatedAt(message.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMddhhmm")));
            msgDto.setIsMine(message.getFromUser().getId() == id); // 보낸사람이 현재 사용자면 true, 아니면 false
            return msgDto;
        }).collect(Collectors.toList());
        return res;
    }

    /*
    특정 상대방과의 모든 메세지 기록
     */
    public List<MessageDto.Response> getDetailMsg(int userId, int messageId) {
        Message oldMsg = messageRepository.getById(messageId);
        int fromId = oldMsg.getFromUser().getId();
        int toId = oldMsg.getToUser().getId();
        int otherId;
        if (fromId == userId) {
            otherId = toId;
        } else {
            otherId = fromId;
        }
        List<Message> messages = messageRepository.findDetailMessage(userId, otherId);
        List<MessageDto.Response> res = messages.stream().map(message -> {
            MessageDto.Response msgDto = modelMapper.map(message, MessageDto.Response.class);
            msgDto.setMessageId(message.getId());
            msgDto.setCreatedAt(message.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMddhhmm")));
            msgDto.setIsMine(message.getFromUser().getId() == userId); // 보낸사람이 현재 사용자면 true, 아니면 false
            return msgDto;
        }).collect(Collectors.toList());
        return res;
    }

    /*
    메세지 작성
    자기 자신일 경우 작성되지 않음
    탈퇴한 회원에게 쪽지 보낼 수 없음
     */
    public HashMap<String, Object> writeNewMsg(int userId, MessageDto.Request req) {
        HashMap<String, Object> res = new HashMap();
        // 쪽지 받을 사용자 먼저 찾기
        User toUser;
        if (req.getGroup() == 0) {
            Comment comment = commentRepository.getById(req.getId());
            toUser = comment.getUser();
        } else if (req.getGroup() == 1) {
            Message message = messageRepository.getById(req.getId());
            int fromId = message.getFromUser().getId();
            if (fromId == userId) {
                toUser = message.getToUser();
            } else {
                toUser = message.getFromUser();
            }
        } else {
            Board board = boardRepository.getById(req.getId());
            toUser = board.getUser();
        }
        // 탈퇴하지 않은 사용자인지 확인
        if (toUser != null) {
            // 사용자 본인이 아닌지 확인
            User fromUser = userRepository.getById(userId);
            if (toUser != fromUser) {
                // 쪽지 보내기
                Message message = new Message();
                message.setContent(req.getContent());
                message.setFromUser(fromUser);
                message.setToUser(toUser);
                messageRepository.save(message);
                res.put("state", true);
                MessageDto.SuccessResponse sucRes = new MessageDto.SuccessResponse(message.getId());
                res.put("msg_id", sucRes);
            } else {
                res.put("state", false);
                res.put("msg", "자기 자신에게 쪽지를 보낼 수 없습니다");
            }
        } else {
            res.put("state", false);
            res.put("msg", "탈퇴한 사용자에게 쪽지를 보낼 수 없습니다");
        }
        return res;
    }



}
