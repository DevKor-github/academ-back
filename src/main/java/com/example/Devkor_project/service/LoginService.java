package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.EmailAuthenticationRequestDto;
import com.example.Devkor_project.dto.EmailCheckRequestDto;
import com.example.Devkor_project.dto.LoginRequestDto;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Code;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CodeRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

@Service
public class LoginService
{
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    /*
        < 로그인 서비스 >
    */
    @Transactional
    public void login(LoginRequestDto dto, HttpServletRequest request)
    {
        // 이메일에 해당하는 계정 존재 여부 체크
        Profile profile = profileRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."));

        // 비밀번호 체크
        if(!encoder.matches(dto.getPassword(), profile.getPassword()))
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");

        // 기존의 세션 파기
        request.getSession().invalidate();

        // 세션 발행
        HttpSession session = request.getSession(true);
        session.setAttribute("email", dto.getEmail());
        session.setAttribute("role", profile.getRole());
        if (dto.isSaved()) {
            session.setMaxInactiveInterval(60 * 60 * 24 * 30);  // 30일
        } else
            session.setMaxInactiveInterval(60 * 60 * 24);       // 24시간

    }

    /*
        < 로그아웃 서비스 >
    */
    @Transactional
    public void logout(HttpServletRequest request)
    {
        // 세션 파기
        try{
            HttpSession session = request.getSession(false);
            if(session != null)
                session.invalidate();
        }
        catch (AppException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR, "예기치 못한 에러가 발생하였습니다.");
        }
    }

    /*
        < 회원가입 서비스 >
    */
    @Transactional
    public void signUp(SignUpRequestDto dto)
    {
        // 이메일 중복 체크
        profileRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, dto.getEmail() + "는 이미 사용 중입니다.");
                });

        // DTO -> Entity 변환
        Profile profile = Profile.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .studentId(dto.getStudentId())
                .grade(dto.getGrade())
                .semester(dto.getSemester())
                .department(dto.getDepartment())
                .role("USER")
                .point(0)
                .build();

        // 해당 Entity를 데이터베이스에 저장
        profileRepository.save(profile);
    }

    /*
        < 이메일 인증번호 발송 서비스 >
    */
    @Transactional
    public void sendAuthenticationNumber(EmailAuthenticationRequestDto dto)
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
            Context context = new Context();
            context.setVariable("authenticationNumber", authenticationNumber);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(dto.getEmail() + "@korea.ac.kr");    // 메일 수신자
            mimeMessageHelper.setSubject("Academ 인증 번호");   // 메일 제목
            mimeMessageHelper.setText(templateEngine.process("sendNumber", context), true);  // 메일 내용
            javaMailSender.send(mimeMessage);

            // 이미 인증번호가 발송된 이메일인 경우, 데이터베이스에서 인증번호 정보 삭제
            Code code = codeRepository.findByEmail(dto.getEmail()).orElse(null);
            if(code != null)
            {
                codeRepository.delete(code);
            }

            // 인증번호를 데이터베이스에 저장
            code = Code.builder()
                    .email(dto.getEmail())
                    .code(authenticationNumber)
                    .createdAt(LocalDate.now())
                    .build();

            codeRepository.save(code);
        }
        catch (AppException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR, "예기치 못한 에러가 발생하였습니다.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        < 이메일 인증번호 확인 서비스 >
    */
    @Transactional
    public boolean checkAuthenticationNumber(EmailCheckRequestDto dto)
    {
        // 이메일 중복 체크
        profileRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, dto.getEmail() + "는 이미 사용 중입니다.");
                });

        // 해당 이메일로 발송된 인증번호가 있는지 체크
        Code code = codeRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.UNEXPECTED_ERROR, "예기치 못한 에러가 발생하였습니다."));

        // 요청으로 받은 인증번호와 데이터베이스에 저장되어있는 인증번호를 비교
        return Objects.equals(dto.getAuthenticationCode(), code.getCode());
    }

    /*
        < 임시 비밀번호 발급 서비스 >
    */
    public void resetPassword(EmailAuthenticationRequestDto dto)
    {
        // 이메일에 해당하는 계정 존재 여부 체크
        Profile profile = profileRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_UNAUTHORIZED, dto.getEmail() + "에 해당하는 계정이 존재하지 않습니다."));

        // 임시 비밀번호 생성
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            char word = (char)(random.nextInt(26) + 'a');
            randomString.append(word);
        }
        String newPassword = randomString.toString();

        // 기존의 계정을 데이터베이스에서 삭제
        profileRepository.delete(profile);

        // 임시 비밀번호를 비밀번호로 하는 새로운 계정을 데이터베이스에 반영
        profile.setPassword(encoder.encode(newPassword));
        profileRepository.save(profile);

        // 임시 비밀번호를 이메일로 전송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            Context context = new Context();
            context.setVariable("newPassword", newPassword);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(dto.getEmail());    // 메일 수신자
            mimeMessageHelper.setSubject("Academ 임시 비밀번호 발급");   // 메일 제목
            mimeMessageHelper.setText(templateEngine.process("newPassword", context), true);  // 메일 내용
            javaMailSender.send(mimeMessage);
        }
        catch (AppException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR, dto.getEmail() + "예기치 못한 에러가 발생하였습니다.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
