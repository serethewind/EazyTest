package com.easytest.examsession.feignClient;

import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import com.easytest.examsession.feignClient.dto.QuestionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "question-bank", path = "api/v1/question")
public interface FeignClientInterceptor {

    @GetMapping("generate") //generate question for the quiz service
    ResponseEntity<List<Long>> generateSetOfQuestions(@RequestParam String category, @RequestParam Integer numberOfQuestions);

//    @GetMapping("retrieve-generated-questions")
//    ResponseEntity<List<QuestionResponseDto>> getQuestionsBasedOnId(@RequestBody List<Long> questionIds);

    @GetMapping("get-score")
    ResponseEntity<Integer> getScore(@RequestBody List<AnswerResponseDto> responses);

    @PostMapping("retrieve-generated-questions")
    ResponseEntity<List<QuestionResponseDto>> getQuestionsBasedOnId(@RequestBody List<Long> questionIds);
}
