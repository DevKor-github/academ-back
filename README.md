# Academ (Back-end)
Academ Back-end repository입니다.

---

### 프로젝트 구조
( 최신화 : v1.1.4 )
```
│
├── .github
│   └── workflows
│       ├── deploy.yml      // CD 워크플로우 파일
│       └── gradle.yml      // CI 워크플로우 파일
│
├── scripts
│   ├── start.sh    // CD 관련 파일 (새로운 서버 실행)
│   └── stop.sh     // CD 관련 파일 (기존 서버 중단)
│
├── appsepc.yml     // CD 설정 파일 (start.sh/stop.sh 제어)
│
└── src
    └── main
        ├── java.com.example.Devkor_project
        │   │
        │   │   ## 기본적인 설정 파일들
        │   ├── configuartion
        │   │   ├── RedisConfig         // Redis 설정 클래스
        │   │   ├── SwaggerConfig       // Swagger 설정 클래스
        │   │   ├── VersionProvider     // 버전 공급 클래스
        │   │   └── WebConfig           // CORS 설정 클래스
        │   │
        │   │   ## 컨트롤러 파일들
        │   ├── controller
        │   │   ├── AdminController     // Admin 권한 계정만 요청 가능한 api 컨트롤러
        │   │   ├── CourseController    // 강의, 강의평 관련 api 컨트롤러
        │   │   ├── LoginController     // 로그인, 권한 관련 api 컨트롤러
        │   │   ├── MyPageController    // 마이페이지 관련 api 컨트롤러
        │   │   └── VersionController   // 버전 관련 api 컨트롤러
        │   │
        │   │   ## dto 파일들
        │   ├── dto
        │   │   ├── CommentDto          // 강의평 관련 dto
        │   │   ├── CourseDto           // 강의 관련 dto
        │   │   ├── CrawlingDto         // 웹 크롤링 관련 dto
        │   │   ├── ProfileDto          // 계정 관련 dto
        │   │   ├── ResponseDto         // 응답 dto
        │   │   └── TokenDto            // access token, refresh token dto
        │   │
        │   │   ## entity 파일들
        │   ├── entity
        │   │   ├── Bookmark            // 북마크 정보 entity
        │   │   ├── Code                // 인증번호 entity
        │   │   ├── Comment             // 강의평 entity
        │   │   ├── CommentLike         // 강의평 좋아요 정보 entity
        │   │   ├── CommentRating       // 강의평 평점 entity
        │   │   ├── CommentReport       // 강의평 신고 정보 entity
        │   │   ├── Course              // 강의 entity
        │   │   ├── CourseRating        // 강의 평점 entity
        │   │   ├── Profile             // 계정 entity
        │   │   └── Traffic             // 트래픽  entity
        │   │
        │   │   ## 예외 처리 파일들
        │   ├── exception
        │   │   ├── AppException        // 커스텀 예외 클래스
        │   │   ├── ErrorCode           // 커스텀 에러 코드 enum
        │   │   └── ExceptionManager    // 예외 처리 설정 클래스
        │   │
        │   │   ## repository 파일들
        │   ├── repository
        │   │   ├── BookmarkRepository              // 북마크 정보 repository
        │   │   ├── CodeRepository                  // 인증번호 repository
        │   │   ├── CommentLikeRepository           // 강의평 좋아요 정보 repository
        │   │   ├── CommentRatingRepository         // 강의평 평점 repository
        │   │   ├── CommentReportRepository         // 강의평 신고 정보 repository
        │   │   ├── CommentRepository               // 강의평 repository
        │   │   ├── CourseRatingRepository          // 강의 평점 repository
        │   │   ├── CourseRepository                // 강의 repository
        │   │   ├── ProfileRepository               // 계정 repository
        │   │   └── TrafficRepository               // 트래픽 repository
        │   │
        │   │   ## 보안 설정 파일들 (Spring Security)
        │   ├── security
        │   │   ├── AccessAuthFilter                // 강의 열람권 보유 여부 검증 필터 클래스
        │   │   ├── CustomAccessDeniedHandler       // 권한 부족 예외 처리 클래스
        │   │   ├── CustomAuthenticationEntryPoint  // 비로그인 사용자 요청 처리 클래스
        │   │   ├── CustomAuthFailureHandler        // 로그인 실패 처리 클래스
        │   │   ├── CustomAuthSuccessHandler        // 로그인 성공 처리 클래스
        │   │   ├── CustomLogoutSuccessHandler      // 로그아웃 성공 처리 클래스
        │   │   ├── CustomUserDetails               // Spring Security 커스텀 프로필 클래스
        │   │   ├── CustomUserDetailsService        // Spring Security 커스텀 메서드 클래스
        │   │   ├── EncoderConfig                   // 인코더 설정 클래스
        │   │   ├── JwtAuthFilter                   // JWT 토큰 유효성 검증 필터
        │   │   ├── JwtUtil                         // JWT 토큰 관련 유틸 함수 클래스
        │   │   ├── SecurityConfig                  // 통합 보안 관리 클래스
        │   │   └── TrafficFilter                   // 트래픽 기록 필터 클래스
        │   │
        │   │   ## 서비스 파일들
        │   └── controller
        │       ├── AdminService        // Admin 권한 계정만 요청 가능한 api 서비스
        │       ├── CourseService       // 강의, 강의평 관련 api 서비스
        │       ├── LoginService        // 로그인, 권한 관련 api 서비스
        │       ├── MyPageService       // 마이페이지 관련 api 서비스
        │       └── SchedulerService    // 인증번호 자동 삭제 서비스
        │
        └── resources
            ├── email_image
            │   └── logo.png        // 이메일 전송 시, 첨부하는 로고 이미지
            └── application.yml     // 프로젝트 설정 파일
```
###

---

### API 명세서
https://www.notion.so/API-b95b340bd8b64f038c2d6a3a0156edff
###

---

### 데이터베이스
https://dbdiagram.io/d/Academ-6644b1eff84ecd1d2240076c
###

---

### 배포 관련 설정
https://www.notion.so/aws-8a16e766baac4f9498cc1e46383b5e2d
###
