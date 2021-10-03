package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.LoginDto;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

    }

    public Optional<UserDto> userInfoLookup(int id) {
        Optional<User> user = userRepository.findById(id);
        UserDto userDto = new UserDto();
        if (user.isPresent()) {
            userDto = modelMapper.map(user.get(), UserDto.class);
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    public String userJoin(User user) {
        // user_id duplicate check
        if (userRepository.existsUserByUserId(user.getUserId())){
            return "아이디가 중복되었습니다";
        }
        // email duplicate check
        if (userRepository.existsUserByEmail(user.getEmail())) {
            return "이메일이 중복되었습니다";
        }
        // nickname duplicate check
        if (userRepository.existsUserByNickname(user.getEmail())) {
            return "닉네임이 중복되었습니다";
        }
        // hp duplicate check
        if (userRepository.existsUserByHp(user.getHp())) {
            return "전화번호가 중복되었습니다";
        }
        // password encode
        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);
        userRepository.save(user);
        return "";
    }

    /*
    닉네임 변경
     */
    public Optional<User> updateNickname(int id, String newNick) {
        // 닉네임 중복 테스트
        if (userRepository.existsUserByNickname(newNick)) {
            // 중복
            return Optional.empty();
        }
        // 변경
        User user = userRepository.getById(id);
        user.setNickname(newNick);
        userRepository.save(user);
        return Optional.of(user);
    }

    /*
    회원 탈퇴
     */
    public void deleteUser(int id) {
        userRepository.delete(userRepository.getById(id));
    }

    // 로그인
    public Optional<User> login(LoginDto loginDto) {
        if (userRepository.existsUserByUserId(loginDto.getUserId())) {
            User findUser = userRepository.getUserByUserId(loginDto.getUserId());
            // 아이디 옳으면
            boolean checkPassword = passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword());
            if (checkPassword) {
                // 로그인 성공
                return Optional.of(findUser);
            }
        }
        return Optional.empty();
    }


}
