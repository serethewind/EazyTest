package com.easytest.examsession.entity;

import com.easytest.examsession.dto.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "exam_records")
public class ExamEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;



}
