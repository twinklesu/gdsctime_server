package seoultech.gdsc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seoultech.gdsc.web.dto.LikedDto;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.FailResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;
import seoultech.gdsc.web.service.LikedService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/like")
public class LikedController {
    private final LikedService likedService;
    private final HttpSession session;

    @Autowired
    public LikedController(LikedService likedService,
                           HttpSession session) {
        this.likedService = likedService;
        this.session = session;
    }

    /*
    좋아요눌렀을 때
     */
    @PostMapping("")
    public BasicResponse postLiked(@RequestBody LikedDto.Request request) {
        int sessionId = (int) session.getAttribute("springSes");
        String result = likedService.saveLike(sessionId, request);
        if (result.equals("")) {
            return new SuccessResponse<>(new EmptyJsonResponse());
        }
        return new FailResponse<>(result, new EmptyJsonResponse());
    }

}
