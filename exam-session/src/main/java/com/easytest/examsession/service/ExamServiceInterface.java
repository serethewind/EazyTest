package com.easytest.examsession.service;

import com.easytest.examsession.dto.ExamRequestDto;
import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.dto.ResponseDto;
import com.easytest.examsession.dto.communicaton.AnswerResponseDto;

public interface ExamServiceInterface {
    ResponseDto createQuizSession(ExamRequestDto examRequestDto);
    ExamResponseDto fetchExamSessionById(Long examId);

    Integer getScoreForExam(Long examId, AnswerResponseDto answerResponseDto); //can return a well documented response instead of an integer

}
