package seoultech.gdsc.web.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserDtoTest extends WebApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Transactional
    public void dtoReadTest() {
        User newUser = new User();
        newUser.setName("박수빈");
        newUser.setEmail("twinklesu914@gmail.com");
        newUser.setUserId("twinklesu");
        newUser.setHp("010-3081-1524");
        newUser.setMajor("itm");
        newUser.setPassword("990104");
        newUser.setNickname("숩니");
        User saved_user = userRepository.save(newUser);

        Optional<User> user = userRepository.findById(saved_user.getId());
//            Optional<User> user = userRepository.findById(2);
        if (user.isPresent()) {
            UserDto userDto = modelMapper.map(user.get(), UserDto.class);
            assertThat(userDto.getName()).isEqualTo(newUser.getName());
            assertThat(userDto.getEmail()).isEqualTo(newUser.getEmail());
            assertThat(userDto.getUserId()).isEqualTo(newUser.getUserId());
            assertThat(userDto.getHp()).isEqualTo(newUser.getHp());
            assertThat(userDto.getNickname()).isEqualTo(newUser.getNickname());
            assertThat(userDto.getIsAuth()).isEqualTo(newUser.getIsAuth());
            assertThat(userDto.getProfilePic()).isEqualTo(newUser.getProfilePic());
        } else {
            System.out.println("#############################################");
            System.out.println("UserDtoTest: dtoReadTest Fail");
        }
    }
}
