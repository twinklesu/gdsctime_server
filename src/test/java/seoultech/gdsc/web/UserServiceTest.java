package seoultech.gdsc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    @Transactional
    public void userInfoLookupTest() {
        Optional<UserDto> userDto = userService.userInfoLookup(1);
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
    public void saveUserTest() throws JsonProcessingException {
        User newUser = new User();
        newUser.setName("Subin Park");
        newUser.setEmail("twinklesu14@gmail.com");
        newUser.setUserId("twinklesu14");
        newUser.setHp("010-3081-1525");
        newUser.setMajor("itm");
        newUser.setPassword("990104");
        newUser.setNickname("subin");
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
    public void loginTest() {
        System.out.println("#########UserServiceTest:loginTest###########");
        System.out.println("#########success test############");
        LoginDto loginDto = new LoginDto("twinklesu1", "00000");
        String message = userService.login(loginDto);
        System.out.println("expect:   , actual: " + message);
        System.out.println("############end#############");
        System.out.println("#########wrong password test############");
        loginDto.setPassword("000");
        message = userService.login(loginDto);
        System.out.println("expect: 일치하지 않는 회원정보입니다., actual: " + message);
        System.out.println("############end#############");
        System.out.println("#########wrong id test############");
        loginDto.setUserId("tttt");
        message = userService.login(loginDto);
        System.out.println("expect: 일치하지 않는~~~, actual: " + message);
        System.out.println("########---END---#########");
    }
}
