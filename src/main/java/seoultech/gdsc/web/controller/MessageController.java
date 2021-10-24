package seoultech.gdsc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seoultech.gdsc.web.dto.MessageDto;
import seoultech.gdsc.web.entity.Message;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.FailResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;
import seoultech.gdsc.web.service.MessageService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;
    private final HttpSession session;

    @Autowired
    public MessageController(MessageService messageService,
                             HttpSession session){
        this.messageService = messageService;
        this.session = session;
    }

    /*
    메세지 목록 조회
     */
    @GetMapping("")
    public BasicResponse getAllRecentMsg(){
        int sessionId = (int) session.getAttribute("springSes");
        List<MessageDto.Response> res = messageService.getAllRecentMsg(sessionId);
        return new SuccessResponse<>(res);
    }

    /*
    특정 사용자와의 전체 쪽지 목록 조회
     */
    @GetMapping("/detail/{messageId}")
    public BasicResponse getDetailMsg(@PathVariable int messageId){
        int sessionId = (int) session.getAttribute("springSes");
        List<MessageDto.Response> res = messageService.getDetailMsg(sessionId, messageId);
        return new SuccessResponse<>(res);
    }

    /*
    메세지 작성
     */
    @PostMapping("")
    public BasicResponse postMessage(@RequestBody MessageDto.Request request){
        int sessionId = (int) session.getAttribute("springSes");
        HashMap<String, Object> result = messageService.writeNewMsg(sessionId, request);
        if ((Boolean) result.get("state")) {
            return new SuccessResponse<>(result.get("msg_id"));
        }
        return new FailResponse<>((String) result.get("msg"), new EmptyJsonResponse());
    }
}
