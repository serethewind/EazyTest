package com.easytest.examsession.service;

import com.easytest.examsession.dto.ExamRequestDto;
import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.dto.ResponseDto;
import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExamServiceInterface {
    ResponseDto createQuizSession(ExamRequestDto examRequestDto);
    ExamResponseDto getQuestionsForExamSession(Long examId);

    Integer calculateScoreForExamSession(Long examId, List<AnswerResponseDto> answerResponseDto); //can return a well documented response instead of an integer

    ResponseEntity<ResponseDto> generateQuestionsFallbackMethod(ExamRequestDto examRequestDto, Throwable throwable);

    ResponseEntity<ResponseDto> populateQuestionsFallbackMethod(Long examId, Throwable throwable);
    ResponseEntity<ResponseDto> retrieveScoreFallbackMethod(Long examId, List<AnswerResponseDto> answerResponseDto, Throwable throwable);
    List<ExamResponseDto> fetchExamSession();
}
