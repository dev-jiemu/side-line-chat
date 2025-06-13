package com.sideline.chat.user.repository;

import com.sideline.chat.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserIdAndDeleteYn(String userId, String deleteYn);
}
