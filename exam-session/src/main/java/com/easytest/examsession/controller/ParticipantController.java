package com.easytest.examsession.controller;

import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import com.easytest.examsession.service.ExamServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/quiz-session/participant")
public class ParticipantController {
    private ExamServiceInterface examService;

    @GetMapping("welcome")
    public ResponseEntity<String> getWelcome(){
        return ResponseEntity.ok("Welcome");
    }

    @PostMapping("/{id}")
    public ResponseEntity<ExamResponseDto> fetchExamSessionById(@PathVariable("id") Long examId){
        return new ResponseEntity<>(examService.getQuestionsForExamSession(examId), HttpStatus.OK);
    }

    @GetMapping("{id}/get-score")
    public ResponseEntity<Integer> submitAndGetScoreForExamSession(@PathVariable("id") Long examId, @RequestBody List<AnswerResponseDto> answerResponseDto){
        return new ResponseEntity<>(examService.calculateScoreForExamSession(examId, answerResponseDto), HttpStatus.OK);
    }
}
