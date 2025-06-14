package com.sideline.chat.room.repository;

import com.sideline.chat.room.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findRoomByContactIdAndDeleteYnAndRoomType(String contactId, String deleteYn, String roomType);
    ChatRoom findRoomByRoomIdAndDeleteYn(Long roomId, String deleteYn);
}
