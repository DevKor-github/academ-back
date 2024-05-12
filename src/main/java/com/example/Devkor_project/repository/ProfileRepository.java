package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>
{
    Optional<Profile> findByEmail(String email);
}
