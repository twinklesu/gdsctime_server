package seoultech.gdsc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;
import seoultech.gdsc.web.dto.LoginDto;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.FailResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;
import seoultech.gdsc.web.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final HttpSession session;

    @Autowired
    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    /*
    사용자 정보 조회
     */
    @GetMapping("")
    public BasicResponse getUser() {
        int id = (int) session.getAttribute("springSes");
        Optional<UserDto.Response> userDto = userService.userInfoLookup(id);
        if (userDto.isPresent()) {
            return new SuccessResponse<>(userDto.get());
        }
        return new FailResponse<>("");
    }

    /*
    회원가입
     */
    @PostMapping("")
    public BasicResponse saveUser(@RequestBody() UserDto.Request user){
        String message = userService.userJoin(user);
        if (message.equals("")) {
            return new SuccessResponse<>(new EmptyJsonResponse());
        }
        else {
            return new FailResponse<>(message);
        }
    }

    /*
    회원정보 수정
     */
    @PutMapping("")
    public BasicResponse putUser(@RequestBody User user) {
        int id = (int) session.getAttribute("springSes");
        Optional<User> updatedUser = userService.updateNickname(id, user.getNickname());
        if (updatedUser.isPresent()) {
            return new SuccessResponse<>(new EmptyJsonResponse());
        } else {
            return new FailResponse<>("닉네임이 중복되었습니다");
        }
    }

    /*
    회원 탈퇴
     */
    @DeleteMapping("")
    public BasicResponse deleteUser() {
        int id = (int) session.getAttribute("springSes");
        userService.deleteUser(id);
        return new SuccessResponse<>(new EmptyJsonResponse());
    }

    /*
    로그인
     */
    @PostMapping("/login")
    public BasicResponse login(@RequestBody LoginDto loginDto) {
        Optional<User> user = userService.login(loginDto);
        if (user.isPresent()) {
            session.setAttribute("springSes", user.get().getId());
            return new SuccessResponse<>(new EmptyJsonResponse());
        }
        else {
            return new FailResponse<>("일치하지 않는 회원정보 입니다");
        }
    }

    /*
    로그아웃
     */
    @GetMapping("/logout")
    public BasicResponse logout() {
        int id = (int) session.getAttribute("springSes");
        session.invalidate();
        return new SuccessResponse<>(new EmptyJsonResponse());
    }
}
