package com.sideline.chat.room.repository;

import com.sideline.chat.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomByContactIdAndDeleteYnAndRoomType(String contactId, String deleteYn, String roomType);
    Room findRoomByRoomIdAndDeleteYn(Long roomId, String deleteYn);
}
