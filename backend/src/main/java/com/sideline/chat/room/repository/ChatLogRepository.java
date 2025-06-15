package com.sideline.chat.room.repository;

import com.sideline.chat.room.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    List<ChatLog> findChatLogByRoomIdAndDeleteYnOrderBySendAtAsc(Long roomId, String deleteYn);
}
