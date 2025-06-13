package com.sideline.chat.user.controller;

import com.sideline.chat.user.dto.BaseResponse;
import com.sideline.chat.user.dto.UserRequest;
import com.sideline.chat.user.dto.UserResponse;
import com.sideline.chat.user.entity.User;
import com.sideline.chat.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<BaseResponse<UserResponse>> login(@RequestBody UserRequest userRequest) {
        log.info("user login request : " + userRequest.toString());

        BaseResponse response;

        if (userRequest.getUserId() == null || userRequest.getUserId().isEmpty()) {
            response = new BaseResponse<>("user_id required");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userService.getUserOne(userRequest.getUserId()).get();
        if (user == null) {
            response = new BaseResponse<>("user is not found");
            return ResponseEntity.badRequest().body(response);
        }

        if (!user.getPassword().equals(userRequest.getPassword())) {
            response = new BaseResponse<>("user password not match");
            return ResponseEntity.badRequest().body(response);
        }

        response = new BaseResponse<>("");
        UserResponse userResponse = new UserResponse();
        userResponse.setAuthType(user.getAuthType());
        response.setData(userResponse);

        return ResponseEntity.ok(response);
    }
}
