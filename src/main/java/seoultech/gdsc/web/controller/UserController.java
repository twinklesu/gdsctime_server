package seoultech.gdsc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seoultech.gdsc.web.dto.LoginDto;
import seoultech.gdsc.web.dto.UserDto;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.response.BasicResponse;
import seoultech.gdsc.web.response.FailResponse;
import seoultech.gdsc.web.response.SuccessResponse;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;
import seoultech.gdsc.web.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("")
    public BasicResponse getUser() {
        int id = 1; // 세션해서 넣어줘야해..
        Optional<UserDto> userDto = userService.userInfoLookup(id);
        if (userDto.isPresent()) {
            return new SuccessResponse<>(userDto.get());
        }
        return new FailResponse<>("");
    }

    @PostMapping("")
    public BasicResponse saveUser(@RequestBody() User user){
        String message = userService.userJoin(user);
        if (message.equals("")) {
            return new SuccessResponse<User>(null);
        }
        else {
            return new FailResponse<User>(message);
        }
    }


    // login
    @PostMapping("/login")
    public BasicResponse login(@RequestBody LoginDto loginDto) {
        String message = userService.login(loginDto);
        if (message.equals("")) {
            return new SuccessResponse<>(new EmptyJsonResponse());
        }
        else {
            return new FailResponse<>(message);
        }
    }
}
