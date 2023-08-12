package com.easytest.examsession.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseDto {
    private String responseCode;
    private String responseMessage;
}
