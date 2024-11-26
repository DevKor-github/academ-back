package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Profile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>
{
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByUsername(String username);

    @Query(value = "SELECT * FROM profile WHERE profile_id = 0 AND username = '알 수 없음'", nativeQuery = true)
    Optional<Profile> getUnknownProfile();
}
