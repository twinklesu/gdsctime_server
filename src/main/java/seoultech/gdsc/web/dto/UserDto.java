package seoultech.gdsc.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String userId;
    private String email;
    private String hp;
    private String name;
    private String nickname;
    private Boolean isAuth; // boolean 사용하면 getter 가 틀어져서 ㅜㅜ
    private String profilePic;
}
