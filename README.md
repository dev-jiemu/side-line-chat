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
    room_id BIGINT PRIMARY KEY,
    contact_id VARCHAR(50), -- FK to user(user_id)
    room_type VARCHAR(10), -- 'main' or 'side'
    created_at TIMESTAMP,
    closed_at TIMESTAMP, -- 종료 시각
    delete_yn CHAR(1) -- 'Y' or 'N'
);

CREATE TABLE chat_room_link (
    user_id VARCHAR(50), -- main 방 기준 user_id
    side_room_id BIGINT, -- 연결된 side 채팅방 room_id
    delete_yn CHAR(1) -- 'Y' or 'N'
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
| /room              | POST   | 메인 채팅방 생성 (일반 사용자 -> 상담사) |
| /room/:id          | POST   | 사이드 채팅방 생성 (옵저버 -> 상담사)  |
| /room/:id          | DELETE | 메인 채팅방 삭제                |
| /room/:id/:side_id | DELETE | 사이드 채팅방 삭제               |