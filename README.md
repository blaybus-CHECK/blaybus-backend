# 블레이버스_무브텀 API 명세

## ✅ 회원 가입 기능

### 요청

```
메서드: POST
경로: /api/member/signUp
헤더: Content-Type: application/json
요청 본문:

CreateMemberCommand {
	email: string,
	password: string
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/api/member/signUp' \
-H 'Content-Type: application/json' \
-d '{
"email": "member123@example.com",
"password": "password123"
}'
```

### 응답

```
상태 코드: 204 No Content
응답 본문: 없음
```

## ✅ 로그인 기능

### 요청

```
메서드: POST
경로: /api/member/issueToken
헤더: Content-Type: application/json
요청 본문:

IssueMemberToken {
	email: string,
	password: string
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/api/member/issueToken' \
-H 'Content-Type: application/json' \
-d '{
"email": "member123@example.com",
"password": "password123"
}'
```

### 응답

```
상태 코드: 200 OK
응답 본문:

AccessTokenCarrier {
	accessToken: string
}
```

## ✅ 회원 정보 조회
### 요청
```
메서드: GET
경로: /api/member/me
헤더: Authorization: Bearer {accessToken}
```

### curl 명령 예시
```bash
curl -i -X GET 'http://localhost:8080/api/member/me' \
-H 'Authorization: Bearer {accessToken}'
```

### 응답
```
MemberMeView {
    id: string,
    email: string,
}
```
### 테스트
-[x] 올바르게 요청하면 200 OK 상태코드를 반환한다
-[x] 접근 토큰을 사용하지 않으면 401 Unauthorized 상태코드를 반환한다
-[x] 서로 다른 회원의 식별자는 서로 다르다
-[x] 같은 회원의 식별자는 항상 같다
-[x] 회원의 기본 정보가 올바르게 설정된다

## ✅ 대표 작품 등록
### 요청

```
메서드: POST
경로: /api/main-work
헤더: Authorization: Bearer {accessToken}
요청 본문:
RegisterMainWorkCommand {
    title: string,
    description: string
}
```
### curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/api/main-work' \
-H 'Authorization: Bearer {accessToken}' \
-H 'Content-Type: application/json' \
-d '{
"title": "My First Main Work",
"description": "This is a description of my first main work."
}'
```

### 응답

```
상태 코드: 201 Created
응답 본문: 없음
```

### 테스트
- [x] 올바르게 요청하면 201 Created 상태코드를 반환한다
- [x] 제목이 지정되지 않을 경우 400 Bad Request 상태코드를 반환한다
- [x] 제목이 빈 문자열인 경우 400 Bad Request 상태코드를 반환한다
- [x] 설명이 지정되지 않을 경우 400 Bad Request 상태코드를 반환한다
- [x] 설명이 빈 문자열인 경우 400 Bad Request 상태코드를 반환한다
- [x] 설명이 1000자 이상인 경우 400 Bad Request 상태코드를 반환한다
- [x] 이미지 url이 5개 이상인 경우 400 Bad Request 상태코드를 반환한다
- [x] 올바르게 요청하면 등록된 대표 작품 정보에 접근하는 Location 헤더를 반환한다

## ✅ 대표 작품 조회
### 요청
```
메서드: GET
경로: /api/main-work/{id}
헤더: Authorization: Bearer {accessToken}
```
### curl 명령 예시
```
curl -i -X GET 'http://localhost:8080/api/main-work/{id}' \
-H 'Authorization: Bearer {accessToken}'
```
### 응답
```
MainWorkView {
    id: string(UUID),
    title: string,
    description: string
}
```
### 테스트
- [x] 올바르게 요청하면 200 OK 상태코드를 반환한다
- [x] 존재하지 않는 대표 작품 식별자를 사용하면 404 Not Found 상태코드를 반환한다
- [x] 다른 회원이 등록한 대표 작품 식별자를 사용하면 404 Not Found 상태코드를 반환한다
- [x] 대표 작품 식별자를 올바르게 반환한다
- [x] 대표 작품 정보를 올바르게 반환한다