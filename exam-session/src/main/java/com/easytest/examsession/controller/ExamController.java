package com.easytest.examsession.controller;

import com.easytest.examsession.dto.ExamRequestDto;
import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.dto.ResponseDto;
import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import com.easytest.examsession.service.ExamServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz-session")
@AllArgsConstructor
public class ExamController {

    private ExamServiceInterface examService;


    /**
     * create quiz, which will be a compliation of questions
     * get quiz (which can be called by both the admin and the user)
     * get score (which can be called by both the user and the admin)
     */

    @PostMapping("create-quiz")
    public ResponseEntity<ResponseDto> createQuizSession(@RequestBody ExamRequestDto examRequestDto) {
        return new ResponseEntity<>(examService.createQuizSession(examRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ExamResponseDto> fetchExamSessionById(@PathVariable ("id") Long examId){
        return new ResponseEntity<>(examService.getQuestionsForExamSession(examId), HttpStatus.OK);
    }

    @GetMapping("{id}/get-score")
    public ResponseEntity<Integer> submitAndGetScoreForExamSession(@PathVariable("id") Long examId, @RequestBody List<AnswerResponseDto> answerResponseDto){
        return new ResponseEntity<>(examService.calculateScoreForExamSession(examId, answerResponseDto), HttpStatus.OK);
    }

}
