package com.easytest.questionbank.service;

import com.easytest.questionbank.dto.QuestionRequestDto;
import com.easytest.questionbank.dto.QuestionResponseDto;
import com.easytest.questionbank.dto.ResponseDto;
import com.easytest.questionbank.dto.communication.AnswerResponseDto;

import java.util.List;

public interface QuestionServiceInterface {

    List<QuestionResponseDto> getAllQuestions();

    List<QuestionResponseDto> findQuestionsByCategory(String category);

    ResponseDto addQuestion(QuestionRequestDto questionRequestDto);

    ResponseDto updateQuestion(QuestionRequestDto questionRequestDto, Long questionId);

    ResponseDto deleteQuestion(Long questionId);

    List<Long> generateQuestionsForQuiz(String category, Integer numberOfQuestions);

    QuestionResponseDto getQuestionById(Long id);

    List<QuestionResponseDto> getQuestionsBasedOnId(List<Long> questionIds);

    Integer getScores(List<AnswerResponseDto> responses);
}
