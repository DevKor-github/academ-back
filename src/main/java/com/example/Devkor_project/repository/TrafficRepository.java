package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrafficRepository extends JpaRepository<Traffic, Long>
{
    @Query(value = "SELECT * FROM traffic WHERE api_path = :api_path AND year = :year AND month = :month", nativeQuery = true)
    Traffic searchTraffic(@Param("api_path") String api_path, @Param("year") String year, @Param("month") Byte month);
}
