package com.sideline.chat.user.service;

import com.sideline.chat.user.entity.User;
import com.sideline.chat.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserOne(String userId) {
        // 삭제 안된 계정 정보만
        return userRepository.findByUserIdAndDeleteYn(userId, "N");
    }

}
