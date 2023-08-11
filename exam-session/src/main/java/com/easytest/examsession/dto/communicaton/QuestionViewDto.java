package com.easytest.examsession.dto.communicaton;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionViewDto {

    private long id;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
