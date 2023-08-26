package com.easytest.questionbank.controller;

import com.easytest.questionbank.dto.QuestionRequestDto;
import com.easytest.questionbank.dto.QuestionResponseDto;
import com.easytest.questionbank.dto.ResponseDto;
import com.easytest.questionbank.dto.communication.AnswerResponseDto;
import com.easytest.questionbank.service.QuestionServiceInterface;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/question")
@AllArgsConstructor
public class QuestionController {

    private QuestionServiceInterface questionService;

    @GetMapping("welcome")
    public ResponseEntity<String> getWelcome(){
        return ResponseEntity.ok("Welcome");
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestions(){
        return new ResponseEntity<>(questionService.getAllQuestions(), HttpStatus.OK);
    }

    @GetMapping("category")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionByCategory(@RequestParam("category") String category){
        return new ResponseEntity<>(questionService.findQuestionsByCategory(category), HttpStatus.OK);
    }

    @PostMapping("/create-question")
    public ResponseEntity<ResponseDto> createQuestion(@RequestBody QuestionRequestDto questionRequestDto){
        return new ResponseEntity<>(questionService.addQuestion(questionRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/create-multiple-questions")
    public ResponseEntity<List<ResponseDto>> createMultipleQuestions(@RequestBody List<QuestionRequestDto> questionRequestDtoList){
        return new ResponseEntity<>(questionService.addMultipleQuestions(questionRequestDtoList), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseDto> updateQuestion(@RequestBody QuestionRequestDto questionRequestDto, @PathVariable("id") Long questionId){
        return new ResponseEntity<>(questionService.updateQuestion(questionRequestDto, questionId), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDto> deleteQuestion(@PathVariable("id") Long questionId){
        return new ResponseEntity<>(questionService.deleteQuestion(questionId), HttpStatus.OK);
    }

    @GetMapping("generate") //generate question for the quiz service
    public ResponseEntity<List<Long>> generateSetOfQuestions(@RequestParam String category, @RequestParam Integer numberOfQuestions){
        return new ResponseEntity<>(questionService.generateQuestionsForQuiz(category, numberOfQuestions), HttpStatus.OK);
    }

    @GetMapping("retrieve-generated-questions")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsBasedOnId(@RequestBody List<Long> questionIds){
        return new ResponseEntity<>(questionService.getQuestionsBasedOnId(questionIds), HttpStatus.OK);
    }

    @PostMapping("get-score")
    public ResponseEntity<Integer> getScore(@RequestBody List<AnswerResponseDto> responses){
        return new ResponseEntity<>(questionService.getScores(responses), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionByQuestionId(@PathVariable("id") Long id){
        return new ResponseEntity<>(questionService.getQuestionById(id), HttpStatus.OK);
    }
}
