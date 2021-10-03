package seoultech.gdsc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.dto.LoginDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.UserRepository;
import seoultech.gdsc.web.service.UserService;

import javax.servlet.http.Cookie;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends WebApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private UserService userService;

    private int sessionId;
    private User newUser;

    @Transactional
    public void before() throws Exception {
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
        loginDto.setPassword("990104");
        Optional<User> loggedUser = userService.login(loginDto);
        loggedUser.ifPresent(user -> {
            sessionId = user.getId();
            session.setAttribute("springSec", sessionId);
        });
    }


    @Transactional
    @Test
    public void getUserTest() throws Exception {
        session.setAttribute("springSes", 1);

        String url = "/api/user";

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("#########UserControllerTest: getUserTest##########");
                    System.out.println(response.getContentAsString()); // fail or success return
                    System.out.println("#########end##########");
                });
    }

    @Test
    @Transactional
    public void saveUserTest() throws Exception {
        String url = "/api/user";

        String request = "{\"user_id\": \"twinklesu\", " +
                "\"password\": \"00000\"," +
                "\"email\": \"twinklesu914@gmail.com\"," +
                "\"name\": \"Subin Park\"," +
                "\"nickname\": \"subin\","+
                "\"major\": \"itm\"," +
                "\"hp\": \"010-3081-1524\"" +
                "}";

        System.out.println("#########UserControllerTest: saveUserTest##########");
        System.out.print("Request: ");
        System.out.println(request);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("Response: ");
                    System.out.println(response.getContentAsString());
                });
        System.out.println("#########end##########");
    }

    /*
    회원정보 수정 아 컨트롤러는 그냥 postman 해야겟어 개빡쳐.....
     */
//    @Test
//    @Transactional
//    public void putUserTest() throws Exception {
//        newUser = new User();
//        newUser.setName("Subin Park");
//        newUser.setEmail("twinklesu14@gmail.com");
//        newUser.setUserId("twinklesu14");
//        newUser.setHp("010-3081-1525");
//        newUser.setMajor("itm");
//        newUser.setPassword("990104");
//        newUser.setNickname("subin");
//        userService.userJoin(newUser);
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUserId(newUser.getUserId());
//        loginDto.setPassword("990104");
//        Optional<User> loggedUser = userService.login(loginDto);
//        loggedUser.ifPresent(user -> {
//            sessionId = user.getId();
//            session.setAttribute("springSec", sessionId);
//        });
//        String url = "/api/user";
//
//        String request = "{\"nickname\": \"subin_new\"}";
//
//        System.out.println("########UserControllerTest: putUser test#########");
//        System.out.println("Request: " + request);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(url)
//                .content(request)
//                .session(session)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(result -> {
//                    MockHttpServletResponse response = result.getResponse();
//                    System.out.println("Response: " + response.getContentAsString());
//                });
//        System.out.println("##########end##############");
//    }


    // login
    @Test
    @Transactional
    public void loginTest() throws Exception {
        String url = "/api/user/login";

        String request = "{\"user_id\": \"twinklesu1\", \"password\": \"00000\"}";

        System.out.println("#########UserControllerTest: login success test##########");
        System.out.println("Request: "+ request);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("response: " + response);
                });


        request = "{\"user_id\": \"twinklesu1\", \"password\": \"000\"}";

        System.out.println("#########UserControllerTest: login password fail test##########");
        System.out.println("Request: "+ request);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("response: " + response.getContentAsString());
                });

        request = "{\"user_id\": \"tttttt\", \"password\": \"000\"}";

        System.out.println("#########UserControllerTest: login id fail test##########");
        System.out.println("Request: "+ request);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("response: " + response.getContentAsString());
                });
    }
}