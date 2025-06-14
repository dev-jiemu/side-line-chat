package com.sideline.chat.room.controller;

import com.sideline.chat.room.dto.RoomRequest;
import com.sideline.chat.room.dto.RoomResponse;
import com.sideline.chat.room.entity.ChatRoom;
import com.sideline.chat.room.service.RoomService;
import com.sideline.chat.common.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private com.sideline.chat.room.dto.ChatRoom chatRoom;

    // TODO : room-link service

    @GetMapping(value="/{id}")
    public ResponseEntity<BaseResponse> getRoom(@PathVariable String id){
        log.info("get room by id : {}", id);

        BaseResponse response;

        if (id.isEmpty()) {
            response = new BaseResponse<>("room_id required");
            return ResponseEntity.badRequest().body(response);
        }


        response = new BaseResponse<>(null);

        // TODO :

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
        response.setData(roomResponse);

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

        // TODO :

        response = new BaseResponse<>(null);
        return ResponseEntity.ok(response);
    }

}
