package com.easytest.questionbank.service;

import com.easytest.questionbank.dto.QuestionRequestDto;
import com.easytest.questionbank.dto.QuestionResponseDto;
import com.easytest.questionbank.dto.ResponseDto;
import com.easytest.questionbank.dto.communication.AnswerResponseDto;
import com.easytest.questionbank.entity.QuestionEntity;
import com.easytest.questionbank.repository.QuestionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class QuestionService implements QuestionServiceInterface {
    private ModelMapper modelMapper;

    private QuestionRepository questionRepository;

    @Override
    @CircuitBreaker(name = "fetchingAllQuestions", fallbackMethod = "fetchingAllQuestionsFallbackMethod")
    @Retry(name = "fetchingAllQuestions", fallbackMethod = "fetchingAllQuestionsFallbackMethod")
    public List<QuestionResponseDto> getAllQuestions() {
        return questionRepository.findAll().stream().map(question ->
                QuestionResponseDto.builder()
                        .id(question.getId())
                        .title(question.getTitle())
                        .option1(question.getOption1())
                        .option2(question.getOption2())
                        .option3(question.getOption3())
                        .option4(question.getOption4())
                        .build()
        ).collect(Collectors.toList());
    }
    @Override
    public ResponseEntity<ResponseDto> fetchingAllQuestionsFallbackMethod(Throwable throwable){
        return new ResponseEntity<>(ResponseDto.builder()
                .responseCode("")
                .responseMessage("Oops!, Fetching all questions from repository taking too long, destination-service will be back up soon")
                .build(), HttpStatus.REQUEST_TIMEOUT);
    }

    @Override
    public List<QuestionResponseDto> findQuestionsByCategory(String category) {
        return questionRepository.findAll().stream().filter(questionEntity -> String.valueOf(questionEntity.getExamCategory()).equalsIgnoreCase(category)).map(question ->
                QuestionResponseDto.builder()
                        .title(question.getTitle())
                        .option1(question.getOption1())
                        .option2(question.getOption2())
                        .option3(question.getOption3())
                        .option4(question.getOption4())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public ResponseDto addQuestion(QuestionRequestDto questionRequestDto) {
        QuestionEntity questionEntity = QuestionEntity.builder()
                .title(questionRequestDto.getTitle())
                .examCategory(questionRequestDto.getExamCategory())
                .option1(questionRequestDto.getOption1())
                .option2(questionRequestDto.getOption2())
                .option3(questionRequestDto.getOption3())
                .option4(questionRequestDto.getOption4())
                .rightAnswer(questionRequestDto.getRightAnswer())
                .difficultyLevel(questionRequestDto.getDifficultyLevel())
                .build();

        questionRepository.save(questionEntity);
        return ResponseDto.builder()
                .id(questionEntity.getId())
                .responseCode("")
                .responseMessage("")
                .build();
    }

    @Override
    public List<ResponseDto> addMultipleQuestions(List<QuestionRequestDto> questionRequestDtoList) {
        return questionRequestDtoList.stream().map((questionRequestDto) -> questionRepository.save(
                 QuestionEntity.builder()
                         .title(questionRequestDto.getTitle())
                         .examCategory(questionRequestDto.getExamCategory())
                         .option1(questionRequestDto.getOption1())
                         .option2(questionRequestDto.getOption2())
                         .option3(questionRequestDto.getOption3())
                         .option4(questionRequestDto.getOption4())
                         .rightAnswer(questionRequestDto.getRightAnswer())
                         .difficultyLevel(questionRequestDto.getDifficultyLevel())
                         .build()
         )).map(questionEntity -> ResponseDto.builder()
                .id(questionEntity.getId())
                .responseCode("")
                .responseMessage("")
                .build()).collect(Collectors.toList());
    }

    @Override
    public ResponseDto updateQuestion(QuestionRequestDto questionRequestDto, Long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(questionRequestDto, questionEntity);
        questionRepository.save(questionEntity);

        return ResponseDto.builder()
                .id(questionEntity.getId())
                .responseCode("")
                .responseMessage("")
                .build();
    }

    @Override
    public ResponseDto deleteQuestion(Long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        questionRepository.delete(questionEntity);
        return ResponseDto.builder()
                .id(questionEntity.getId())
                .responseCode("")
                .responseMessage("")
                .build();
    }

    @Override
    @CircuitBreaker(name = "generating_questions_for_exam_instance", fallbackMethod = "generatingQuestionsFallbackMethod")
    @Retry(name = "generating_questions_for_exam_instance", fallbackMethod = "generatingQuestionsFallbackMethod")
    public List<Long> generateQuestionsForQuiz(String category, Integer numberOfQuestions) {
        List<Long> listOfQuestions = questionRepository.findAll().stream().filter(questionEntity -> String.valueOf(questionEntity.getExamCategory()).equalsIgnoreCase(category)).map(QuestionEntity::getId).collect(Collectors.toList());
        Collections.shuffle(listOfQuestions);
        return listOfQuestions.subList(0, Math.min(numberOfQuestions, listOfQuestions.size()));
    }

    @Override
    public ResponseEntity<ResponseDto> generatingQuestionsFallbackMethod(String category, Integer numberOfQuestions, Throwable throwable){
        return new ResponseEntity<>(ResponseDto.builder()
                .responseCode("")
                .responseMessage("Oops!, Generating questions for exam instance too long, destination-service will be back up soon")
                .build(), HttpStatus.REQUEST_TIMEOUT);
    }


    @Override
    public QuestionResponseDto getQuestionById(Long id) {
        QuestionEntity questionEntity = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        QuestionResponseDto questionResponseDto = modelMapper.map(questionEntity, QuestionResponseDto.class);
        return questionResponseDto;
    }

    @Override
    @CircuitBreaker(name = "populating_question_using_question_id", fallbackMethod = "populatingQuestionsFallbackMethod")
    @Retry(name = "populating_question_using_question_id", fallbackMethod = "populatingQuestionsFallbackMethod")
    public List<QuestionResponseDto> getQuestionsBasedOnId(List<Long> questionIds) {
        return questionIds.stream().map(id -> questionRepository.findById(id).get()).map(question -> QuestionResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .option1(question.getOption1())
                .option2(question.getOption2())
                .option3(question.getOption3())
                .option4(question.getOption4())
                .build()).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ResponseDto> populatingQuestionsFallbackMethod(List<Long> questionIds, Throwable throwable){
        return new ResponseEntity<>(ResponseDto.builder()
                .responseCode("")
                .responseMessage("Oops!, Populating questions request taking too long, destination-service will be back up soon")
                .build(), HttpStatus.REQUEST_TIMEOUT);
    }

    @Override
    public Integer getScores(List<AnswerResponseDto> responses) {
        Integer rightAnswer = 0;


        for (AnswerResponseDto response : responses){
            QuestionEntity questionEntity = questionRepository.findById(response.getId()).get();
            if(response.getResponse().equalsIgnoreCase(questionEntity.getRightAnswer())){
                rightAnswer++;
            }
        }

        return rightAnswer;
    }

}
