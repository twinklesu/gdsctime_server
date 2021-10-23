package seoultech.gdsc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seoultech.gdsc.web.dto.MessageDto;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.service.MessageService;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    @GetMapping("/detail/{other}")
    public BasicResponse getDetailMsg(@PathVariable int other){
        int sessionId = (int) session.getAttribute("springSes");
        List<MessageDto.Response> res = messageService.getDetailMsg(sessionId, other);
        return new SuccessResponse<>(res);
    }

}
