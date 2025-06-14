package com.sideline.chat.room.service;

import com.sideline.chat.room.entity.ChatRoom;
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
    public List<ChatRoom> getMainRoomListForContactId(String contactId) {
        return roomRepository.findRoomByContactIdAndDeleteYnAndRoomType(contactId, "N", "main");
    }

    // room id 로 조회
    public ChatRoom getRoomOne(Long roomId) {
        return roomRepository.findRoomByRoomIdAndDeleteYn(roomId, "N");
    }

    public ChatRoom createRoom(String contactId, String senderId, String roomType) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setContactId(contactId);
        chatRoom.setSenderId(senderId);
        chatRoom.setRoomType(roomType);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setDeleteYn("N");

        return roomRepository.save(chatRoom);
    }
}
