package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Code;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CodeRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    /* 회원가입 서비스 */
    @Transactional
    public void signUp(SignUpRequestDto dto)
    {
        // 이메일 중복 체크
        profileRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, dto.getEmail());
                });

        // DTO -> Entity 변환
        Profile profile = Profile.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .student_id(dto.getStudent_id())
                .degree(dto.getDegree())
                .semester(dto.getSemester())
                .department(dto.getDepartment())
                .role("ROLE_USER") // Role auto mapping
                .point(0)
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
            String content = String.format("Color.you <br> 인증 번호 <br><br> %s", authenticationNumber);

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
        catch (AppException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR, email + "@korea.ac.kr");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
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
        Code Actualcode = codeRepository.findByEmail(email + "@korea.ac.kr")
                .orElseThrow(() -> new AppException(ErrorCode.CODE_NOT_FOUND, email + "@korea.ac.kr"));

        // 입력한 인증번호가 맞는지 체크
        if(!Objects.equals(code, Actualcode.getCode()))
            throw new AppException(ErrorCode.WRONG_CODE, email + "@korea.ac.kr");
    }

    /* 임시 비밀번호 발급 서비스 */
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

        // 기존의 계정을 데이터베이스에서 삭제
        profileRepository.delete(profile);

        // 임시 비밀번호를 비밀번호로 하는 새로운 계정을 데이터베이스에 반영
        profile.setPassword(encoder.encode(newPassword));
        profileRepository.save(profile);

        // 임시 비밀번호를 이메일로 전송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            String content = String.format("Color.you <br> 임시 비밀번호 <br><br> %s", newPassword);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email + "@korea.ac.kr");    // 메일 수신자
            mimeMessageHelper.setSubject("Academ 임시 비밀번호 발급");   // 메일 제목
            mimeMessageHelper.setText(content, true);  // 메일 내용
            javaMailSender.send(mimeMessage);
        }
        catch (AppException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR, email + "@korea.ac.kr");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
