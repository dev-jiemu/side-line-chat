package com.sideline.chat.room.service;

import com.sideline.chat.room.entity.ChatRoomLink;
import com.sideline.chat.room.repository.ChatRoomLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomLinkService {

    @Autowired
    private ChatRoomLinkRepository chatRoomLinkRepository;

    public List<ChatRoomLink> getSideRoomList(Long mainRoomId) {
        return chatRoomLinkRepository.findByMainRoomId(mainRoomId);
    }

    public ChatRoomLink getChatRoomLink(Long linkSeqno) {
        return chatRoomLinkRepository.findById(linkSeqno).get();
    }

    public void createChatRoomLink(ChatRoomLink chatRoomLink) {
        chatRoomLinkRepository.save(chatRoomLink);
    }
}
