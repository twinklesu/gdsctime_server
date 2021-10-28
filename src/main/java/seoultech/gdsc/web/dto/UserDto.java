package seoultech.gdsc.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request {
        private String userId;
        private String password;
        private String email;
        private String name;
        private String nickname;
        private String major;
        private String hp;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private String userId;
        private String email;
        private String name;
        private String nickname;
        private Boolean isAuth;
        private String profilePic;
    }


}
