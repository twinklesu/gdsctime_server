package seoultech.gdsc.web.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.controller.UserController;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.UserRepository;
import seoultech.gdsc.web.response.BasicResponse;

import javax.persistence.Basic;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends WebApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @BeforeEach
    public void beforeEach(){
//        User newUser = new User();
//        newUser.setName("Subin Park");
//        newUser.setEmail("twinklesu914@gmail.com");
//        newUser.setUserId("twinklesu");
//        newUser.setHp("010-3081-1524");
//        newUser.setMajor("itm");
//        newUser.setPassword("990104");
//        newUser.setNickname("subin");
//        userRepository.save(newUser);
    }

    @Test
    public void getUserTest() throws Exception {
        String url = "/api/user";

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("#########UserControllerTest: getUserTest##########");
                    System.out.println(response.getContentAsString()); // fail or success return
                    System.out.println("#########end##########");
                });
    }
}