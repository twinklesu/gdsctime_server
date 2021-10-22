package seoultech.gdsc.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.User;

import java.time.LocalDateTime;

public class CommentDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private int id;
        private String nickname;
        private String profilePic;
        private String content;
        private int likeNum;
        private int userId;
        private String createdAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request {
        private int boardId;
        private String content;
        private Boolean isSecret;
    }
}
