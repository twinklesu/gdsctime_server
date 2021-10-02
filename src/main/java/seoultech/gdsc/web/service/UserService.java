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

    // 로그인
    public String login(LoginDto loginDto) {
        if (userRepository.existsUserByUserId(loginDto.getUserId())) {
            User findUser = userRepository.getUserByUserId(loginDto.getUserId());
            // 아이디 옳으면
            boolean checkPassword = passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword());
            if (checkPassword) {
                // 로그인 성공
                // 아무래도 여기서 세션 처리 해야하지 않을까?????
                return "";
            }
        }
        return "일치하지 않는 회원정보 입니다.";
    }
}
