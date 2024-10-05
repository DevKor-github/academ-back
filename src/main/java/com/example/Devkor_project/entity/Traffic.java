package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Traffic
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traffic_id;

    @Column(nullable = false) private String api_path;
    @Column(nullable = false) private String year;
    @Column(nullable = false) private Byte month;
    @Column(nullable = false) private int count;
}
