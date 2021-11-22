package seoultech.gdsc.web.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserRepositoryTest extends WebApplicationTests {

    @Autowired
    private UserRepository userRepository;




    private User newUser;

    @BeforeEach
    public void beforeEach(){
        newUser = new User();
        this.newUser.setName("Subin Park");
        this.newUser.setEmail("twinklesu914@gmail.com");
        this.newUser.setUserId("twinklesu");
        this.newUser.setMajor("itm");
        this.newUser.setPassword("990104");
        this.newUser.setNickname("subin");
        this.userRepository.save(newUser);
    }

    @Test
    public void getUserTest() {
        Optional<User> getUser = userRepository.findById(this.newUser.getId());
        if (getUser.isPresent()) {
            assertThat(getUser.get().getUserId()).isEqualTo(this.newUser.getUserId());
            assertThat(getUser.get().getName()).isEqualTo(this.newUser.getName());
            assertThat(getUser.get().getMajor()).isEqualTo(this.newUser.getMajor());
            assertThat(getUser.get().getNickname()).isEqualTo(this.newUser.getNickname());
            assertThat(getUser.get().getPassword()).isEqualTo(this.newUser.getPassword());
        } else {
            System.out.println("#############################################");
            System.out.println("UserRepositoryTest: getUserTest Fail");
        }
//        getUser.ifPresent(user -> assertThat(user).isEqualTo(this.newUser));
    }

    @Test
    @Transactional
    public void saveUserTest(){

        newUser = new User();
        this.newUser.setName("Subin Park");
        this.newUser.setEmail("twinklesu914@gmail.com");
        this.newUser.setUserId("twinklesu");
        this.newUser.setMajor("itm");
        this.newUser.setPassword("990104");
        this.newUser.setNickname("subin");

        this.userRepository.save(newUser);
    }

}
