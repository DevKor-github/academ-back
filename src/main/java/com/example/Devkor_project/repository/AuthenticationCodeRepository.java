package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface AuthenticationCodeRepository extends JpaRepository<Code, Long>
{
    Optional<Code> findByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM code WHERE created_at < :date", nativeQuery = true)
    void deleteOldCode(LocalDate date);
}
