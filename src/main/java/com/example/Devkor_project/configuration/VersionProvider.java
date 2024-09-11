package com.example.Devkor_project.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// version 관리

@Getter
@Component
public class VersionProvider {
    @Value("${academ.version}")
    private String version;
}
