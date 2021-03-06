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
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper,
                       BoardRepository boardRepository,
                       MessageRepository messageRepository,
                       CommentRepository commentRepository,
                       LikedRepository likedRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.boardRepository = boardRepository;
        this.messageRepository = messageRepository;
        this.commentRepository = commentRepository;
        this.likedRepository = likedRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Optional<UserDto.Response> userInfoLookup(int id) {
        Optional<User> user = userRepository.findById(id);
        UserDto.Response userDto;
        if (user.isPresent()) {
            userDto = modelMapper.map(user.get(), UserDto.Response.class);
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    public String userJoin(UserDto.Request user) {
        // user_id duplicate check
        if (userRepository.existsUserByUserId(user.getUserId())){
            return "???????????? ?????????????????????";
        }
        // email duplicate check
        if (userRepository.existsUserByEmail(user.getEmail())) {
            return "???????????? ?????????????????????";
        }
        // nickname duplicate check
        if (userRepository.existsUserByNickname(user.getNickname())) {
            return "???????????? ?????????????????????";
        }
        User newUser = modelMapper.map(user, User.class);
        String encodePassword = passwordEncoder.encode(user.getPassword());

        newUser.setPassword(encodePassword);
        userRepository.save(newUser);
        return "";
    }

    /*
    ????????? ??????
     */
    public Optional<User> updateNickname(int id, String newNick) {
        // ????????? ?????? ?????????
        if (userRepository.existsUserByNickname(newNick)) {
            // ??????
            return Optional.empty();
        }
        // ??????
        User user = userRepository.getById(id);
        user.setNickname(newNick);
        userRepository.save(user);
        return Optional.of(user);
    }

    /*
    ?????? ??????
     */
    public void deleteUser(int id) {
        // ???????????? ????????? ????????? delete_at ?????? ?????? ????????????????????? ?????? ?????? ?????? ?????????
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

    // ?????????
    public Optional<User> login(LoginDto loginDto) {
        if (userRepository.existsUserByUserId(loginDto.getUserId())) {
            User findUser = userRepository.getUserByUserId(loginDto.getUserId());
            // ????????? ?????????
            boolean checkPassword = passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword());
            if (checkPassword) {
                // ????????? ??????
                return Optional.of(findUser);
            }
        }
        return Optional.empty();
    }


}
