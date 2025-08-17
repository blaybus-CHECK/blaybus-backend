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