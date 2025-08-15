# 블레이버스_무브텀 서비스 HTTP 서버

## API 목록

### 회원 가입 기능

요청

- 메서드: POST
- 경로: /api/member/signup
- -헤더
  ```
  Content-Type: application/json
  ```
- 요청 본문
  ```
    CreateMemberCommand {
        email: string,
        password: string
    }
    ```
- curl 명령 예시
- ```bash
  curl -i -X POST 'http://localhost:8080/api/member/signUp' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "seller1@example.com",
    "password": "seller1-password"
  }'
    ```
성공 응답
- 상태코드: 204 No Content
- 본문: 없음

### 프로필 등록 기능

요청

- 메서드: POST
- 경로: /api/profile/register
- 헤더
  ```
  Content-Type: application/json
  ```
- 요청 본문
  ```
    RegisterProfileCommand {
    artistIntro: string,
    artistCareer: number,
    portfolioUrl: string,
    contact: string,
    email: string,
    snsUrl: string 
  }
  ```
- curl 명령 예시
  ```bash
    curl -i -X POST 'http://localhost:8080/api/profile/register' \
    -H 'Content-Type: application/json' \
    -d '{
      "artistIntro": "안녕하세요, 저는 블레이버스입니다.",
      "artistCareer": 5,
      "portfolioUrl": "http://example.com/portfolio",
      "contact": "010-1234-5678",
      "email": "test@test.com",
    }'
  ```

성공 응답

- 상태코드: 204 No Content
- 본문: 없음

### 프로필 등록 기능 2

요청

- 메서드: POST
- 경로: /api/profile/register
- 헤더
  ```
  Content-Type: application/json
  ```
- 요청 본문
  ```
    RegisterProfileCommand {
    artistIntro: string,
    artistCareer: number,
    portfolioUrl: string,
    contact: string,
    email: string,
    snsUrl: string,
    mainWorkTitle: string,
    mainWorkContent: string,
  }
  ```
- curl 명령 예시
  ```bash
    curl -i -X POST 'http://localhost:8080/api/profile/register' \
    -H 'Content-Type: application/json' \
    -d '{
      "artistIntro": "안녕하세요, 저는 블레이버스입니다.",
      "artistCareer": 5,
      "portfolioUrl": "http://example.com/portfolio",
      "contact": "010-1234-5678",
      "email": "test@test.com",
    }'
  ```

성공 응답

- 상태코드: 204 No Content
- 본문: 없음

### 파일 요청 테스트 api

- curl 명령 예시
  ```bash
   curl -i -X POST -F file=@/Users/gogam/hello.txt "http://localhost:8080/api/file/upload"
  ```