package com.easytest.examsession.feignClient;

import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import com.easytest.examsession.feignClient.dto.QuestionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "question-bank", path = "api/v1/question")
public interface FeignClientInterceptor {

    @GetMapping("generate") //generate question for the quiz service
    public ResponseEntity<List<Long>> generateSetOfQuestions(@RequestParam String category, @RequestParam Integer numberOfQuestions);

    @GetMapping("retrieve-generated-questions")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsBasedOnId(@RequestBody List<Long> questionIds);

    @PostMapping("get-score")
    public ResponseEntity<Integer> getScore(@RequestBody List<AnswerResponseDto> responses);

}
