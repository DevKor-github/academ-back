package com.example.Devkor_project.security;

import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.Devkor_project.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // profile 데이터베이스에서 해당 이메일의 계정 검색
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILURE, email));

        // 해당 이메일의 계정이 존재하면, Spring security에서 제공하는 User 클래스를 빌드
        return new CustomUserDetails(profile);
    }

    public UserDetails loadUserByProfileId(Long profile_id) throws UsernameNotFoundException
    {
        // profile 데이터베이스에서 해당 아이디의 계정 검색
        Profile profile = profileRepository.findById(profile_id)
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILURE, null));

        // 해당 이메일의 계정이 존재하면, Spring security에서 제공하는 User 클래스를 빌드
        return new CustomUserDetails(profile);
    }
}
