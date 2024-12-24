package com.example.Devkor_project.util;

import com.example.Devkor_project.dto.CourseDto;

import java.util.ArrayList;
import java.util.List;

public class TimeLocationParser {

    /**
     * Parses a time_location string to List<CourseDto.TimeLocation>.
     * For example: "화(6-8) 로봇융합관 301호\n목(6)" -> [
     *      {
     *          day: "화"
     *          startPeriod: 6
     *          endPeriod: 8
     *          location: "로봇융합관 301호"
     *      },
     *      {
     *          day: "목"
     *          startPeriod: 6
     *          endPeriod: 6
     *          location: null
     *      }
     * ]
     *
     * @param timeLocations The time_location string to parse.
     * @return List<CourseDto.TimeLocation> representing List of { day, startPeriod, endPeriod, location }.
     */
    public static List<CourseDto.TimeLocation> parseTimeLocation(String timeLocations) {
        if (timeLocations == null || timeLocations.isBlank()) {
            return null;
        }

        List<CourseDto.TimeLocation> timeLocationList = new ArrayList<>();
        String[] lines = timeLocations.split("\\n");

        for (String line : lines) {

            String day = null;
            Integer startPeriod = null;
            Integer endPeriod = null;
            String location = null;

            // 첫 글자가 괄호가 아닐 때
            if (line.charAt(0) != '(') {
                // 첫 글자가 요일이 아니라면, 전체 문자열을 강의실로 간주
                // 첫 글자가 요일이라면, 요일 정보를 추출
                if(line.charAt(0) != '일' && line.charAt(0) != '월' && line.charAt(0) != '화' && line.charAt(0) != '수' && line.charAt(0) != '목' && line.charAt(0) != '금' && line.charAt(0) != '토') {
                    location = line;
                    line = "";
                } else {
                    day = String.valueOf(line.charAt(0));
                    line = line.substring(1);
                }
            }

            // 요일을 추출한 후의 로직 처리
            if(!line.isEmpty()) {
                if(line.charAt(0) != '(') {
                    // 요일을 추출한 후에도 괄호가 없다면, 남은 문자열을 강의실로 간주
                    location = line;
                } else {
                    // 요일을 추출한 후에 괄호가 있다면, 남은 문자열을 (시작교시-끝교시)와 강의실 두 부분으로 분할
                    String[] parts = line.split(" ", 2);

                    // (시작교시-끝교시)에서 앞뒤 괄호 제거
                    parts[0] = parts[0].substring(1, parts[0].length() - 1);

                    // 시작교시와 끝교시 추출
                    if(parts[0].contains("-")) {
                        String[] subparts = parts[0].split("-", 2);
                        startPeriod = Integer.parseInt(subparts[0]);
                        endPeriod = Integer.parseInt(subparts[1]);
                    } else {
                        startPeriod = Integer.parseInt(parts[0]);
                        endPeriod = Integer.parseInt(parts[0]);
                    }

                    // 강의실 추출
                    if(!parts[1].isEmpty()) {
                        location = parts[1];
                    }
                }
            }

            CourseDto.TimeLocation timeLocation = CourseDto.TimeLocation.builder()
                        .day(day) // 요일 (없으면 null)
                        .startPeriod(startPeriod)
                        .endPeriod(endPeriod)
                        .location(location) // 강의실 (없으면 null)
                        .build();

            timeLocationList.add(timeLocation);
        }

        return timeLocationList;
    }
}
