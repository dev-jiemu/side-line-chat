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

    public List<ChatRoom> getMainRoomOneForContactId(String contactId, String roomType) {
        return roomRepository.findChatRoomByContactIdAndDeleteYnAndRoomType(contactId, "N", roomType);
    }

    public List<ChatRoom> getSideRoomList(Long[] roomId) {
        return roomRepository.findByRoomIdInAndDeleteYn(roomId, "N");
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

    public void updateRoom(ChatRoom chatRoom) {
        chatRoom.setDeleteYn("Y");
        roomRepository.save(chatRoom);
    }
}
