package com.sideline.chat.room.controller;

import com.sideline.chat.room.dto.*;
import com.sideline.chat.room.entity.ChatRoom;
import com.sideline.chat.room.entity.ChatRoomLink;
import com.sideline.chat.room.service.ChatRoomLinkService;
import com.sideline.chat.room.service.RoomService;
import com.sideline.chat.common.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ChatSession chatRoom;

    @Autowired
    private ChatRoomLinkService chatRoomLinkService;

    @GetMapping(value="/{roomId}")
    public ResponseEntity<BaseResponse<RoomResponse>> getRoom(@PathVariable String roomId){
        log.info("get room by id : {}", roomId);
        BaseResponse response;

        if (roomId.isEmpty()) {
            response = new BaseResponse<>("room_id required");
            return ResponseEntity.badRequest().body(response);
        }

        ChatRoom roomInfo = roomService.getRoomOne(Long.valueOf(roomId));
        if (roomInfo == null) {
            response = new BaseResponse<>("chat room not found");
            return ResponseEntity.badRequest().body(response);
        }

        response = new BaseResponse<>(null);
        RoomResponse roomResponse = new RoomResponse();

        roomResponse.setRoomId(roomInfo.getRoomId());
        roomResponse.setSenderId(roomInfo.getSenderId());
        roomResponse.setContactId(roomInfo.getContactId());
        roomResponse.setClosedAt(roomInfo.getClosedAt());
        roomResponse.setRoomType(roomInfo.getRoomType());

        response.setData(roomResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping(value="/{userId}/list")
    public ResponseEntity<BaseResponse<List<ChatRoomHierarchyResponse>>> getRoomList(@PathVariable String userId) {
        log.info("get room list by user_id : {}", userId);
        BaseResponse response;

        if (userId == null || userId.isEmpty()) {
            response = new BaseResponse<>("user_id required");
            return ResponseEntity.badRequest().body(response);
        }

        // TODO : 테스트를 위해 일단 하드코딩
        String fixUserId = "contactor";

        List<ChatRoomHierarchyResponse> roomList = new ArrayList<>();

        // main type list
        List<ChatRoom> mainChatList = roomService.getMainRoomOneForContactId(fixUserId, "main");
        if (mainChatList.isEmpty()) {
            response = new BaseResponse<>(null);
            response.setData(roomList);
            return ResponseEntity.ok(response);
        }

        // TODO : 나중에 리팩토링 필요함... 너무 복잡해 ㅠ
        // side list append
        for(ChatRoom chatRoom : mainChatList){
            // 1. main room
            ChatRoomHierarchyResponse roomHierarchy = new ChatRoomHierarchyResponse();

            roomHierarchy.setRoomId(chatRoom.getRoomId());
            roomHierarchy.setSenderId(chatRoom.getSenderId());
            roomHierarchy.setContactId(chatRoom.getContactId());
            roomHierarchy.setClosedAt(chatRoom.getClosedAt());
            roomHierarchy.setRoomType(chatRoom.getRoomType());

            // 2. side room 이 존재하면 하위 정보 구성
            List<ChatRoomLink> sideLinkList = chatRoomLinkService.getSideRoomList(chatRoom.getRoomId());

            if (!sideLinkList.isEmpty()) {
                Long[] sideRooms = new Long[sideLinkList.size()];
                for(int i = 0; i < sideLinkList.size(); i++){
                    sideRooms[i] = sideLinkList.get(i).getSideRoomId();
                }

                // 2. room infomation search
                List<ChatRoom> sideRoomList = roomService.getSideRoomList(sideRooms);
                List<ChatRoomHierarchyResponse> sideRoomHierarchyList = new ArrayList<>();

                for (ChatRoom sideRoom : sideRoomList) {
                    ChatRoomHierarchyResponse side = new ChatRoomHierarchyResponse();

                    side.setRoomId(sideRoom.getRoomId());
                    side.setSenderId(sideRoom.getSenderId());
                    side.setContactId(sideRoom.getContactId());
                    side.setClosedAt(sideRoom.getClosedAt());
                    side.setRoomType(sideRoom.getRoomType());

                    sideRoomHierarchyList.add(side);
                }

                roomHierarchy.setSideRooms(sideRoomHierarchyList);
            }
            roomList.add(roomHierarchy);
        }


        response = new BaseResponse<>(null);
        response.setData(roomList);
        log.info("get room list by user_id : {}", roomList.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse<RoomResponse>> createRoom(@RequestBody RoomRequest roomRequest){
        log.info("create room : {}", roomRequest);
        BaseResponse response;

        if (roomRequest == null) {
            response = new BaseResponse<>("request body is empty");
            return ResponseEntity.badRequest().body(response);
        }

        if (roomRequest.getSender().isEmpty() || roomRequest.getTarget().isEmpty() || roomRequest.getRoomType().isEmpty()) {
            response = new BaseResponse<>("request body is missing");
            return ResponseEntity.badRequest().body(response);
        }

        // 상담원 user_id 가 보통 contactId 로 들어가게 될거임
        ChatRoom chatRoom = roomService.createRoom(roomRequest.getTarget(), roomRequest.getSender(), roomRequest.getRoomType());
        if (chatRoom == null) {
            response = new BaseResponse<>("create room failed");
            return ResponseEntity.badRequest().body(response);
        }

        response = new BaseResponse<>(null);
        RoomResponse roomResponse = new RoomResponse();

        roomResponse.setRoomId(chatRoom.getRoomId());
        roomResponse.setSenderId(chatRoom.getSenderId());
        roomResponse.setContactId(chatRoom.getContactId());
        roomResponse.setClosedAt(chatRoom.getClosedAt());
        roomResponse.setRoomType(chatRoom.getRoomType());

        response.setData(roomResponse);

        log.info("create main room : {}", roomResponse.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/{roomId}")
    public ResponseEntity<BaseResponse<RoomResponse>> createSideRoom(@RequestBody RoomRequest roomRequest, @PathVariable String roomId){
        log.info("create side room : {}, main_room_id : {} ", roomRequest, roomId);
        BaseResponse response;

        // main db insert
        ChatRoom chatRoom = roomService.createRoom(roomRequest.getTarget(), roomRequest.getSender(), roomRequest.getRoomType());
        if (chatRoom == null) {
            response = new BaseResponse<>("create room failed");
            return ResponseEntity.badRequest().body(response);
        }

        ChatRoomLink sideLink = new ChatRoomLink();
        sideLink.setMainRoomId(Long.valueOf(roomId));
        sideLink.setSideRoomId(chatRoom.getRoomId());
        sideLink.setUserId(roomRequest.getSender());

        chatRoomLinkService.createChatRoomLink(sideLink);

        response = new BaseResponse<>(null);
        RoomResponse roomResponse = new RoomResponse();

        roomResponse.setRoomId(chatRoom.getRoomId());
        roomResponse.setSenderId(chatRoom.getSenderId());
        roomResponse.setContactId(chatRoom.getContactId());
        roomResponse.setClosedAt(chatRoom.getClosedAt());
        roomResponse.setRoomType(chatRoom.getRoomType());

        response.setData(roomResponse);

        log.info("create side room : {}", roomResponse.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{roomId}/close")
    public ResponseEntity<String> closeChat(@PathVariable Long roomId) {
        chatRoom.closeRoom(roomId);
        return ResponseEntity.ok("채팅방이 종료되었습니다.");
    }

    @DeleteMapping(value = "/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable Long roomId) {
        log.info("delete room : {}", roomId);
        BaseResponse response;

        if (roomId == null) {
            response = new BaseResponse<>("room_id is empty");
            return ResponseEntity.badRequest().body(response);
        }

        ChatRoom roomInfo = roomService.getRoomOne(roomId);
        if (roomInfo == null) {
            response = new BaseResponse<>("room id not found");
            return ResponseEntity.badRequest().body(response);
        }

        roomService.updateRoom(roomInfo);
        return ResponseEntity.ok().build();
    }

}
