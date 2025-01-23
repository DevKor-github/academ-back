package com.example.Devkor_project.security;

import com.example.Devkor_project.entity.Traffic;
import com.example.Devkor_project.repository.TrafficRepository;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

@RequiredArgsConstructor
public class TrafficFilter extends OncePerRequestFilter {

    private final TrafficRepository trafficRepository;

    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try {
            Traffic traffic = trafficRepository.searchTraffic(request.getRequestURI(), String.valueOf(LocalDate.now().getYear()), (byte) LocalDate.now().getMonthValue());

            // 기존 트래픽 정보가 존재한다면, count 값을 1 증가
            // 기존 트래픽 정보가 존재하지 않는다면, 새롭게 만든 후 count 값을 1로 초기화
            if (traffic != null) {
                traffic.setCount(traffic.getCount() + 1);

                trafficRepository.save(traffic);
            } else {
                Traffic newTraffic = Traffic.builder()
                        .api_path(request.getRequestURI())
                        .year(String.valueOf(LocalDate.now().getYear()))
                        .month((byte) LocalDate.now().getMonthValue())
                        .count(1)
                        .build();

                trafficRepository.save(newTraffic);
            }
        } catch (Exception error) {
            throw new RuntimeException(error);
        }

        filterChain.doFilter(request, response);
    }

    // 필터를 적용하지 않을 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        String path = request.getRequestURI();

        return !path.startsWith("/api");
    }

}
