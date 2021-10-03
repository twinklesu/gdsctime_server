package seoultech.gdsc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.dto.LoginDto;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.FailResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.service.UserService;

import java.util.Optional;

@Transactional
@SpringBootTest
public class UserServiceTest extends WebApplicationTests{

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User newUser;
    private int sessionId;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        newUser = new User();
        newUser.setName("Subin Park");
        newUser.setEmail("twinklesu14@gmail.com");
        newUser.setUserId("twinklesu14");
        newUser.setHp("010-3081-1525");
        newUser.setMajor("itm");
        newUser.setPassword("990104");
        newUser.setNickname("subin");
        userService.userJoin(newUser);
        LoginDto loginDto = new LoginDto();
        loginDto.setUserId(newUser.getUserId());
        loginDto.setPassword(newUser.getPassword());
        Optional<User> loggedUser = userService.login(loginDto);
        loggedUser.ifPresent(user -> {
            sessionId = user.getId();
        });
    }

    @Test
    @Transactional
    public void userInfoLookupTest() {
        Optional<UserDto> userDto = userService.userInfoLookup(newUser.getId());
        System.out.println("########UserServiceTest: userInfoLookupoTest#########");
        if (userDto.isPresent()) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
        System.out.println("########end###########");
    }

    @Test
    @Transactional
    public void userJoinTest() throws JsonProcessingException {
        String joinResult = userService.userJoin(newUser);
        System.out.print("result: ");
        System.out.println(joinResult);
        if (joinResult.equals("")) {
            BasicResponse sucRes = new SuccessResponse<User>(null);
            System.out.println(objectMapper.writeValueAsString(sucRes));
        }
        else {
            System.out.println("가입실패");
        }
    }

    @Test
    @Transactional
    public void updateNicknameTest() {
        String newNick = "subinnnn";
        System.out.println("#########UserServiceTest: updateNicknameTest###########");
        System.out.println("success test");
        Optional<User> updatedUser = userService.updateNickname(sessionId, newNick);
        updatedUser.ifPresent(user -> {
            System.out.println("expected: " + newNick + "actual: "+ user.getNickname());
        });
        System.out.println("FAIL TEST");
        User anotherUser = new User();
        anotherUser.setUserId("temp");
        anotherUser.setPassword("0000");
        anotherUser.setHp("000-0000-0000");
        anotherUser.setMajor("major");
        anotherUser.setEmail("temp@temp.com");
        anotherUser.setName("temp");
        anotherUser.setNickname("subin");
        userService.userJoin(anotherUser);

        Optional<User> updateUser = userService.updateNickname(sessionId, "subin");
        if (updateUser.isPresent()) {
            System.out.println("duplicate nickname test fail");
        } else {
            System.out.println("dupicate nicknmae test success");
        }
        System.out.println("############end#############");
    }

    @Test
    @Transactional
    public void loginTest() {
        System.out.println("#########UserServiceTest:loginTest###########");
        userService.userJoin(newUser);
        System.out.println("#########success test############");
        LoginDto loginDto = new LoginDto("twinklesu14", "990104");
        userService.login(loginDto).ifPresent(user -> {
            try {
                System.out.println("expect: 옳은 유저 리턴 , actual: " + objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println("############end#############");
            System.out.println("#########wrong password test############");
        });
        loginDto.setPassword("000");
        userService.login(loginDto).ifPresent(user -> {
            try {
                System.out.println("expect: 일치하지 않는 회원정보입니다., actual: " + objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        System.out.println("############end#############");
        System.out.println("#########wrong id test############");
        loginDto.setUserId("tttt");
        userService.login(loginDto).ifPresent(user -> {
            try {
                System.out.println("expect: 일치하지 않는 회원정보입니다., actual: " + objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        System.out.println("########---END---#########");
    }
}
