package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.LoginDto;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.*;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final MessageRepository messageRepository;
    private final CommentRepository commentRepository;
    private final LikedRepository likedRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper,
                       BoardRepository boardRepository,
                       MessageRepository messageRepository,
                       CommentRepository commentRepository,
                       LikedRepository likedRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.boardRepository = boardRepository;
        this.messageRepository = messageRepository;
        this.commentRepository = commentRepository;
        this.likedRepository = likedRepository;

    }

    public Optional<UserDto.Response> userInfoLookup(int id) {
        Optional<User> user = userRepository.findById(id);
        UserDto.Response userDto = new UserDto.Response();
        if (user.isPresent()) {
            userDto = modelMapper.map(user.get(), UserDto.Response.class);
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    public String userJoin(UserDto.Request user) {
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
        User newUser = modelMapper.map(user, User.class);
        // password encode
        String encodePassword = passwordEncoder.encode(user.getPassword());

        newUser.setPassword(encodePassword);
        userRepository.save(newUser);
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
        // 탈퇴처리 오바다 이래서 delete_at 두고 글은 남겨두는거구나 디비 접속 너무 많이해
        boardRepository.findAllByUser_Id(id).forEach(board -> {
            board.setUser(null);
            boardRepository.save(board);
        });
        messageRepository.findAllByFromUser_Id(id).forEach(message -> {
            message.setFromUser(null);
            messageRepository.save(message);
        });
        messageRepository.findAllByToUser_Id(id).forEach(message -> {
            message.setToUser(null);
            messageRepository.save(message);
        });
        commentRepository.findAllByUser_Id(id).forEach(comment -> {
            comment.setUser(null);
            commentRepository.save(comment);
        });
        likedRepository.findAllByUser_Id(id).forEach(liked -> {
            liked.setUser(null);
            likedRepository.save(liked);
        });

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
