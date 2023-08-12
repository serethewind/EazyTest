package com.easytest.examsession.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
    private String title;
    private String category;
    @ElementCollection
    private List<Long> questionId;
//    private List<QuestionViewDto> questionViewDtoList = new ArrayList<>();
}
