## side-line-chat

--- 

### 1. DB Schemas
```h2
CREATE TABLE chat_user (
    user_id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255),
    auth_type VARCHAR(20), -- 'observer', 'contact' 등
    created_at TIMESTAMP,
    delete_yn CHAR(1) -- 'Y' or 'N'
);

CREATE TABLE chat_room (
    room_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    contact_id VARCHAR(50), -- FK to user(user_id)
    sender_id VARCHAR(50), -- user_id or customer_id
    room_type VARCHAR(10), -- 'main' or 'side'
    created_at TIMESTAMP,
    closed_at TIMESTAMP, -- 종료 시각
    delete_yn CHAR(1) -- 'Y' or 'N'
);

CREATE TABLE chat_room_link (
    link_seqno BIGINT PRIMARY KEY AUTO_INCREMENT, 
    user_id VARCHAR(50), -- main 방 기준 user_id
    side_room_id BIGINT, -- 연결된 side 채팅방 room_id
    main_room_id BIGINT -- 지정 main room id
);

CREATE TABLE chat_log (
    log_seqno BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id BIGINT, -- FK to chat_room(room_id)
    message VARCHAR(1000),
    sent_at TIMESTAMP, -- 전송 시각
    delete_yn CHAR(1) -- 'Y' or 'N'
);
```


### 2. Backend API Endpoints

| Endpoint           | Method | Description              |
|--------------------|--------|--------------------------|
| /user/login        | POST   | 계정 로그인 요청                |
| /chatRoom              | POST   | 메인 채팅방 생성 (일반 사용자 -> 상담사) |
| /chatRoom/:id          | POST   | 사이드 채팅방 생성 (옵저버 -> 상담사)  |
| /chatRoom/:id          | DELETE | 메인 채팅방 삭제                |
| /chatRoom/:id/:side_id | DELETE | 사이드 채팅방 삭제               |


### 3. Frontend Router List

Router | Description
---|---
/ | 일반 사용자: 채팅 창, 상담사/옵저버: 채팅 리스트 및 채팅창
/login|상담사 또는 옵저버 로그인 창
/chat/:id | 채팅방
