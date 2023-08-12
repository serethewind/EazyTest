package com.easytest.examsession.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamRequestDto {

    private String category;
    private String title;
    private Integer numberOfQuestions;
//    private List<QuestionViewDto> listOfQuestions;

}
