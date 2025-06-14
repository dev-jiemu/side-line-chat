package com.sideline.chat.room.service;

import com.sideline.chat.room.entity.Room;
import com.sideline.chat.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // 상담사별 진행중인 메인 채팅 리스트 조회
    public List<Room> getMainRoomListForContactId(String contactId) {
        return roomRepository.findRoomByContactIdAndDeleteYnAndRoomType(contactId, "N", "main");
    }

    // room id 로 조회
    public Room getRoomOne(Long roomId) {
        return roomRepository.findRoomByRoomIdAndDeleteYn(roomId, "N");
    }

    public Room createRoom(String contactId, String roomType) {
        Room room = new Room();

        room.setContactId(contactId);
        room.setRoomType(roomType);
        room.setCreatedAt(LocalDateTime.now());
        room.setDeleteYn("N");

        return roomRepository.save(room);
    }
}
