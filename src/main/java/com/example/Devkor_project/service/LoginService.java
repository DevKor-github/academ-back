package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.EmailAuthenticationRequestDto;
import com.example.Devkor_project.dto.LoginRequestDto;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
public class LoginService
{
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    /*
        < 로그인 Service >
        이메일이 해당하는 계정이 존재하지 않을 경우,
        비밀번호가 일치하지 않을 경우에 대해서 예외를 발생시킵니다.
    */
    @Transactional
    public void login(LoginRequestDto dto)
    {
        // 이메일에 해당하는 계정 존재 여부 체크
        Profile profile = profileRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."));

        // 비밀번호 체크
        if(!encoder.matches(dto.getPassword(), profile.getPassword()))
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");

    }

    /*
        < 회원가입 Service >
        SignUpRequestDto를 받아서
        해당 이메일이 사용 중일 경우, 예외를 발생시키고,
        해당 이메일이 사용 중이지 않으면, 데이터베이스에 프로필 정보를 저장합니다.
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
                .build();

        // 해당 Entity를 데이터베이스에 저장
        profileRepository.save(profile);
    }

    /*
        < 이메일 인증번호 발송 Service >
        EmailAuthenticationRequestDto를 받아서
        이메일로 랜덤하게 생성된 인증번호(8자리)를 전송합니다.
    */
    public String sendAuthenticationNumber(EmailAuthenticationRequestDto dto)
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
            mimeMessageHelper.setTo(dto.getEmail());    // 메일 수신자
            mimeMessageHelper.setSubject("Academ 인증 번호");   // 메일 제목
            mimeMessageHelper.setText(templateEngine.process("sendNumber", context), true);  // 메일 내용
            javaMailSender.send(mimeMessage);

            return authenticationNumber;
        }
        catch (AppException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR, dto.getEmail() + "예기치 못한 에러가 발생하였습니다.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        < 임시 비밀번호 발급 Service >
        EmailAuthenticationRequestDto를 받아서
        해당 이메일에 해당하는 계정이 존재하지 않는 경우, 예외를 발생시키고,
        그렇지 않다면, 임시 비밀번호(10자리)를 이메일로 전송합니다.
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
