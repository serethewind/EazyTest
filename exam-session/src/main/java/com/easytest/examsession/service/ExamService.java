package com.easytest.examsession.service;

import com.easytest.examsession.dto.ExamRequestDto;
import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.dto.ResponseDto;
import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import com.easytest.examsession.entity.ExamEntity;
import com.easytest.examsession.feignClient.FeignClientInterceptor;
import com.easytest.examsession.repository.ExamRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class ExamService implements ExamServiceInterface{
    private FeignClientInterceptor feignClient;
    private ExamRepository examRepository;

    @Override
    public ResponseDto createQuizSession(ExamRequestDto examRequestDto) {
        //make a call at the question service to retrieve a category of questions with a predefined number of questions
        List<Long> questionIdList= feignClient.generateSetOfQuestions(examRequestDto.getCategory(), examRequestDto.getNumberOfQuestions()).getBody();
        ExamEntity examEntity = ExamEntity.builder()
                .title(examRequestDto.getTitle())
                .category(examRequestDto.getCategory())
                .questionId(questionIdList)
                .build();
        examRepository.save(examEntity);

        return ResponseDto.builder()
                .responseCode("")
                .responseMessage("Successfully created")
                .build();
    }

    @Override
    public ExamResponseDto getQuestionsForExamSession(Long examId) {
       ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

       //return the exam i.e. the title and the actual questions associated with the question Ids stored using feign client
        return ExamResponseDto.builder()
                .title(examEntity.getTitle())
                .listOfQuestions(feignClient.getQuestionsBasedOnId(examEntity.getQuestionId()).getBody())
                .build();
    }

    @Override
    public Integer calculateScoreForExamSession(Long examId, List<AnswerResponseDto> answerResponseDto) {
        ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

       return feignClient.getScore(answerResponseDto).getBody();
    }
}
