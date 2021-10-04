package seoultech.gdsc.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoultech.gdsc.web.entity.User;

import javax.persistence.SecondaryTable;
import java.sql.Timestamp;

public class BoardDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private int boardId;
        private int category;
        private String title;
        private String content;
        private String nickname;
        private String profilePic;
        private int likeNum;
        private int commentNum;
        private String createdAt; // entity로 넘길때는 timestamp로 매핑해주기..
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request {
        private int category;
        private String title;
        private String content;
        private Boolean isSecret;
    }

}
