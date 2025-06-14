package com.sideline.chat.room.repository;

import com.sideline.chat.room.entity.ChatRoomLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomLinkRepository extends JpaRepository<ChatRoomLink, Long> {
    List<ChatRoomLink> findByUserIdAndDeleteYn(String userId, String deleteYn);
}
