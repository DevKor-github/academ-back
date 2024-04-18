package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CustomUserDetails;
import com.example.Devkor_project.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.Devkor_project.entity.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Profile> profileOptional = profileRepository.findByEmail(email);
        System.out.println(profileOptional);

        if (!profileOptional.isPresent()) {
            // System.out.println("Profile not found");
            throw new UsernameNotFoundException(email);
        }

        return new CustomUserDetails(profileOptional.get());
    }
}
