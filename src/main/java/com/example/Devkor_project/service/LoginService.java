package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.dto.TokenDto;
import com.example.Devkor_project.entity.Code;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CodeRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.example.Devkor_project.security.CustomUserDetailsService;
import com.example.Devkor_project.security.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class LoginService
{
    @Autowired ProfileRepository profileRepository;
    @Autowired CodeRepository codeRepository;
    @Autowired BCryptPasswordEncoder encoder;
    @Autowired JavaMailSender javaMailSender;
    @Autowired JwtUtil jwtUtil;
    @Autowired CustomUserDetailsService customUserDetailService;
    @Autowired RedisTemplate<String, String> redisTemplate;

    /* 회원가입 서비스 */
    @Transactional
    public void signUp(ProfileDto.Signup dto)
    {
        // 이메일 중복 체크
        profileRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, dto.getEmail());
                });

        // 해당 이메일로 발송된 인증번호가 있는지 체크
        Code actualCode = codeRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.CODE_NOT_FOUND, dto.getEmail()));

        // 입력한 인증번호가 맞는지 체크
        if(!Objects.equals(dto.getCode(), actualCode.getCode()))
            throw new AppException(ErrorCode.WRONG_CODE, dto.getEmail());

        // 학번이 7자리인지 체크
        if(dto.getStudent_id().length() != 7)
            throw new AppException(ErrorCode.INVALID_STUDENT_ID, dto.getStudent_id());

        // 비밀번호가 8~24자리이고, 숫자와 영문을 포함하는지 체크
        boolean hasEnglish = false;
        boolean hasNumber = false;

        for (int i = 0; i < dto.getPassword().length(); i++) {
            char ch = dto.getPassword().charAt(i);
            if (Character.isLetter(ch)) {
                hasEnglish = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            }

            if (hasEnglish && hasNumber) {
                break;
            }
        }

        if(
            dto.getPassword().length() < 8 ||
            dto.getPassword().length() > 24 ||
            !hasEnglish ||
            !hasNumber
        ) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, dto.getPassword());
        }

        // 닉네임이 1~10자리인지 체크
        if(dto.getUsername().isEmpty() || dto.getUsername().length() > 10)
            throw new AppException(ErrorCode.INVALID_USERNAME, dto.getUsername());

        // 닉네임 중복 체크
        profileRepository.findByUsername(dto.getUsername())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, dto.getUsername());
                });

        // 학위가 'MASTER' 또는 'DEGREE'인지 체크
        if(!Objects.equals(dto.getDegree(), "MASTER") && !Objects.equals(dto.getDegree(), "DOCTOR"))
            throw new AppException(ErrorCode.INVALID_DEGREE, dto.getDegree());

        // DTO -> Entity 변환
        Profile profile = Profile.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .student_id(dto.getStudent_id())
                .degree(dto.getDegree())
                .semester(dto.getSemester())
                .department(dto.getDepartment())
                .role("ROLE_USER")
                .point(0)
                .point_expiration_date(LocalDate.now().plusMonths(1))
                .created_at(LocalDate.now())
                .build();

        // 해당 Entity를 데이터베이스에 저장
        profileRepository.save(profile);
    }

    /* 이메일 인증번호 발송 서비스 */
    @Transactional
    public void sendAuthenticationNumber(String email)
    {
        // 인증번호 생성
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < 8; i++) {
            int word = random.nextInt(10);
            randomNumber.append(word);
        }
        String authenticationNumber = randomNumber.toString();

        // 인증번호 발송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            String content = String.format("Academ <br> 인증 번호 <br><br> %s", authenticationNumber);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email + "@korea.ac.kr");    // 메일 수신자
            mimeMessageHelper.setSubject("Academ 인증 번호");   // 메일 제목
            mimeMessageHelper.setText(content, true);  // 메일 내용
            javaMailSender.send(mimeMessage);

            // 이미 인증번호가 발송된 이메일인 경우, 데이터베이스에서 인증번호 정보 삭제
            Code code = codeRepository.findByEmail(email + "@korea.ac.kr").orElse(null);
            if(code != null)
            {
                codeRepository.delete(code);
            }

            // 인증번호를 데이터베이스에 저장
            code = Code.builder()
                    .email(email + "@korea.ac.kr")
                    .code(authenticationNumber)
                    .created_at(LocalDate.now())
                    .build();

            codeRepository.save(code);
        }
        catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    /* 이메일 인증번호 확인 서비스 */
    @Transactional
    public void checkAuthenticationNumber(String email, String code)
    {
        // 이메일 중복 체크
        profileRepository.findByEmail(email + "@korea.ac.kr")
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, email + "@korea.ac.kr");
                });

        // 해당 이메일로 발송된 인증번호가 있는지 체크
        Code actualCode = codeRepository.findByEmail(email + "@korea.ac.kr")
                .orElseThrow(() -> new AppException(ErrorCode.CODE_NOT_FOUND, email + "@korea.ac.kr"));

        // 입력한 인증번호가 맞는지 체크
        if(!Objects.equals(code, actualCode.getCode()))
            throw new AppException(ErrorCode.WRONG_CODE, email + "@korea.ac.kr");
    }

    /* 임시 비밀번호 발급 서비스 */
    @Transactional
    public void resetPassword(String email)
    {
        // 이메일에 해당하는 계정 존재 여부 체크
        Profile profile = profileRepository.findByEmail(email + "@korea.ac.kr")
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email + "@korea.ac.kr"));

        // 임시 비밀번호 생성
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            char word = (char)(random.nextInt(26) + 'a');
            randomString.append(word);
        }
        String newPassword = randomString.toString();

        // 임시 비밀번호를 비밀번호로 하는 새로 추가할 계정 생성
        Profile newProfile = Profile.builder()
                .email(profile.getEmail())
                .password(encoder.encode(newPassword))
                .username(profile.getUsername())
                .student_id(profile.getStudent_id())
                .degree(profile.getDegree())
                .semester(profile.getSemester())
                .department(profile.getDepartment())
                .point(profile.getPoint())
                .created_at(profile.getCreated_at())
                .role(profile.getRole())
                .build();

        // 기존의 계정을 데이터베이스에서 삭제
        profileRepository.delete(profile);

        // 임시 비밀번호를 비밀번호로 하는 새로운 계정을 데이터베이스에 반영
        profileRepository.save(newProfile);

        // 임시 비밀번호를 이메일로 전송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            String content = String.format("Academ <br> 임시 비밀번호 <br><br> %s", newPassword);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email + "@korea.ac.kr");    // 메일 수신자
            mimeMessageHelper.setSubject("Academ 임시 비밀번호 발급");   // 메일 제목
            mimeMessageHelper.setText(content, true);  // 메일 내용
            javaMailSender.send(mimeMessage);
        }
        catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    /* 로그인 여부 확인 서비스 */
    public ProfileDto.CheckLogin checkLogin(Principal principal)
    {
        // 로그인 여부 확인 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 해당 사용자의 계정이 존재하는지 확인한 뒤, 해당 여부를 반환
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        return ProfileDto.CheckLogin.builder()
                .profile_id(profile.getProfile_id())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .student_id(profile.getStudent_id())
                .degree(profile.getDegree())
                .semester(profile.getSemester())
                .department(profile.getDepartment())
                .point(profile.getPoint())
                .created_at(profile.getCreated_at())
                .role(profile.getRole())
                .build();
    }

    /* access token 재발급 서비스 */
    @Transactional
    public String refreshToken(HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader("Authorization");

        // 헤더에 JWT token이 존재하는지 체크
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if(jwtUtil.validateToken(token))
            {
                Long profile_id = jwtUtil.getProfileId(token);
                UserDetails userDetails = customUserDetailService.loadUserByProfileId(profile_id);

                Profile profile = profileRepository.findById(profile_id)
                        .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, null));

                ProfileDto.Profile profileDto = ProfileDto.Profile.builder()
                        .profile_id(profile.getProfile_id())
                        .email(profile.getEmail())
                        .password(profile.getPassword())
                        .username(profile.getUsername())
                        .created_at(profile.getCreated_at())
                        .role(profile.getRole())
                        .build();

                String email = profile.getEmail();

                if(userDetails != null)
                {
                    // access token 발행
                    TokenDto tokenDto = jwtUtil.returnToken(profileDto, false);

                    // redis에 access token 정보 저장
                    redisTemplate.opsForValue().set(email, tokenDto.getAccessToken());

                    // access token 반환
                    return tokenDto.getAccessToken();
                }
            }
        }

        throw new AppException(ErrorCode.INVALID_TOKEN, null);
    }

}
