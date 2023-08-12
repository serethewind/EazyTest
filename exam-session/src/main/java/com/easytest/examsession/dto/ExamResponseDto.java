package com.easytest.examsession.dto;

import com.easytest.examsession.feignClient.dto.QuestionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamResponseDto {
    private Long id;
    private String title;
    private List<QuestionResponseDto> listOfQuestions;
}
