## side-line-chat
#### 고객-상담사간 메인 채팅방과 옵저버-상담사간 사이드 채팅방 구현 프로젝트
##### 2025.06.13 ~ 2025.06.15 

--- 


### 1. 프로젝트 구조
``` text
side-line-chat/
├── backend/                # Spring Boot 백엔드
│   ├── src/main/java/
│   │   └── com/sideline/chat/
│   │       ├── user/       # 사용자 관리
│   │       ├── room/       # 채팅방 관리 (방 생성 및 채팅 log)
│   │       └── common/     # 공통 구성 요소
│   └── build.gradle
├── frontend/               # Vue.js 프론트엔드
│   ├── src/
│   │   ├── views/          # 페이지 컴포넌트
│   │   ├── components/     # 재사용 컴포넌트
│   │   ├── api/           # API 호출 모듈
│   │   └── router/        # 라우터 설정
│   └── package.json
└── data/                  # H2 데이터베이스 파일
```

- 백엔드 구조
```text
com.sideline.chat/
├── user/
│   ├── controller/     # REST API 컨트롤러
│   ├── service/        # 비즈니스 로직
│   ├── entity/         # JPA 엔티티
│   └── dto/            # 데이터 전송 객체 선언
├── room/
│   ├── controller/     # 채팅방 관련 API
│   ├── service/        # 채팅방 비즈니스 로직
│   ├── entity/         # 채팅방 엔티티
│   ├── dto/           # 채팅방 DTO
│   └── websocket/     # WebSocket 핸들러
└── common/
    └── dto/           # 공통 응답 객체
```

- 프론트엔드 구조
```text
src/
├── views/             # 페이지 컴포넌트
│   ├── Main.vue       # 메인 채팅 페이지
│   └── Login.vue      # 로그인 페이지
├── components/        # 재사용 컴포넌트
│   └── Room.vue       # 채팅방 컴포넌트
├── api/              # API 호출 모듈
│   ├── user.js        # 사용자 API
│   └── room.js        # 채팅방 API
├── router/           # Vue Router 설정
│   └── index.js
├── stores/           # Pinia 스토어
└── assets/           # 정적 자원
```

### 2. 🛠️ 사용 기술 스택
1) Backend
- Framework: Spring Boot 3.5.0
- Database: H2 (In-memory)
- WebSocket: Spring WebSocket
- ORM: Spring Data JPA
- Build Tool: Gradle
- Java Version: 17

2) Frontend
- Framework: Vue.js 3.5.13
- UI Library: Vuetify 3.8.9
- State Management: Pinia 3.0.3
- HTTP Client: Axios 1.9.0
- Router: Vue Router 4.5.1
- Build Tool: Vite 6.2.4


### 3. DB Schemas
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


### 4. 프로젝트 실행
- Backend
```shell
cd backend
./gradlew bootRun
```

Default Port : `8080`
서버 초기 실행시 h2 database 에 상담사, 옵저버 계정이 기본으로 들어갑니다.

* 테스트 계정

`상담원` : contactor / test

`옵저버` : observer / test


- Frontend
```shell
cd frontend
npm install
npm run dev
```

Default Port : `5173`

### 5. 🔗 Backend API Endpoints

- 사용자 관리 Endpoints

| Endpoint               | Method | Description              |
|------------------------|--------|--------------------------|
| /api/user/login        | POST   | 계정 로그인 요청                |

- 채팅 관리 Endpoints

| Endpoint                | Method | Description                                     |
|-------------------------|--------|-------------------------------------------------|
| /api/room/{roomId}      | GET    | 채팅방 정보 조회                                       |
| /api/room/{userId}/list | GET    | 사용자의 채팅방 목록 조회 (* 테스트를 위해 상담원 아이디로 하드코딩 되어있는 상태) |
| /api/room | POST   | 1:1 채팅방 생성(고객 - 상담사)                            |
|/api/room/{roomId} | POST   | 1:1 채팅방 생성(상담사 - 옵저버)                           |
|/api/room/{roomId}/close | DELETE | 채팅방 종료                                          |
|/api/room/{roomId}| DELETE | 사이드 채팅방 삭제                                      |

- 채팅 로그 관리 Endpoints

| Endpoint                | Method | Description                                     |
|-------------------------|--------|-------------------------------------------------|
|/api/logs/{roomId} | GET | 로그 목록 조회 |
|/api/logs/{logSeqno} | DELETE | 채팅 로그 삭제 |


### 6. 🔗 Frontend Router List

Router | Description
---|---
/ | Home (redirect /chat)
/chat | 메인 채팅 페이지 (고객은 채팅 생성 버튼만 노출, 상담사/옵저버는 왼쪽 메뉴에서 채팅 리스트 노출됨)
/agent | 상담사, 옵저버 로그인 페이지 (redirect /login)


### 7. 주요 기능
1) 실시간 채팅
- WebSocket을 통한 실시간 메시지 전송
- 메인 채팅방과 사이드 채팅방 지원
- 채팅방별 사용자 세션 관리

2) 사용자 인증
- 상담사/옵저버 로그인 시스템
- 권한별 접근 제어

3) 채팅방 관리
- 메인 채팅방 생성 (일반 사용자 ↔ 상담사)
- 사이드 채팅방 생성 (옵저버 ↔ 상담사)
- 채팅방 목록 조회 (계층 구조)
- 채팅방 종료/삭제
