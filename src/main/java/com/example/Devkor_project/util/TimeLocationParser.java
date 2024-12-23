package com.example.Devkor_project.util;

import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static List<CourseDto.TimeLocation> parseTimeLocation(String timeLocations)
    {
        if (timeLocations == null || timeLocations.isBlank())
            return null;

        List<CourseDto.TimeLocation> timeLocationList = new ArrayList<>();
        String[] lines = timeLocations.split("\\n");

        // Regular expression to extract start and end periods
        Pattern pattern = Pattern.compile("([가-힣])\\((\\d+)(?:-(\\d+))?\\)(?:\\s(.+))?|([가-힣])\\s?(\\S.*)?|\\((\\d+)-(\\d+)\\)");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line.trim());

            if (matcher.find()) {
                String day = matcher.group(1) != null ? matcher.group(1) : matcher.group(5);  // 요일 추출
                String start = matcher.group(2);
                String end = matcher.group(3);
                String location = matcher.group(4) != null ? matcher.group(4) : matcher.group(6);  // 강의실 추출

                // "요일"과 "시작교시-끝교시"가 모두 없는 경우 처리 (예: (8-10) 형태)
                if (matcher.group(7) != null) {
                    start = matcher.group(7);
                    end = matcher.group(8);
                    day = null;
                    location = null;
                }

                Integer startPeriod = (start != null) ? Integer.parseInt(start) : null;
                Integer endPeriod = (start != null && end != null) ? Integer.valueOf(Integer.parseInt(end)) : startPeriod;

                CourseDto.TimeLocation timeLocation = CourseDto.TimeLocation.builder()
                        .day(day)
                        .startPeriod(startPeriod)
                        .endPeriod(endPeriod)
                        .location(location)
                        .build();

                timeLocationList.add(timeLocation);
            } else {
                throw new AppException(ErrorCode.INVALID_TIME_LOCATION, "Invalid format: " + timeLocations);
            }
        }

        return timeLocationList;
    }
}
