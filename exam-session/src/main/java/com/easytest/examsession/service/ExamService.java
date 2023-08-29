package com.easytest.examsession.service;

import com.easytest.examsession.communicationConfig.RabbitMQProducer;
import com.easytest.examsession.dto.ExamRequestDto;
import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.dto.ResponseDto;
import com.easytest.examsession.dto.communication.EmailDetails;
import com.easytest.examsession.feignClient.dto.AnswerResponseDto;
import com.easytest.examsession.entity.ExamEntity;
import com.easytest.examsession.feignClient.FeignClientInterceptor;
import com.easytest.examsession.feignClient.dto.QuestionResponseDto;
import com.easytest.examsession.repository.ExamRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ExamService implements ExamServiceInterface {
    private FeignClientInterceptor feignClient;
    private ExamRepository examRepository;
    private RabbitMQProducer rabbitMQProducer;

    @Override
    @CircuitBreaker(name = "generateQuestions", fallbackMethod = "generateQuestionsFallbackMethod")
    @Retry(name = "generateQuestions", fallbackMethod = "generateQuestionsFallbackMethod")
    public ResponseDto createQuizSession(ExamRequestDto examRequestDto) {
        //make a call at the question service to retrieve a category of questions with a predefined number of questions
        List<Long> questionIdList = feignClient.generateSetOfQuestions(examRequestDto.getCategory(), examRequestDto.getNumberOfQuestions()).getBody();
        ExamEntity examEntity = ExamEntity.builder()
                .title(examRequestDto.getTitle())
                .category(examRequestDto.getCategory())
                .questionId(questionIdList)
                .build();
        examRepository.save(examEntity);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient("osasereu@gmail.com")
                .message("Hello, you just viewed a new exam session. Do take note of the session id: " + examEntity.getId() + ". Exam session title generated " + examEntity.getTitle())
                .subject("EXAM SESSION SUCCESSFULLY CREATED")
                .build();

        rabbitMQProducer.sendCreateQuizSessionEmailNotification(emailDetails);
        log.info(emailDetails + "created session sent just now");
        return ResponseDto.builder()
                .responseCode("")
                .responseMessage("Successfully created")
                .build();
    }
    @Override
    public ResponseEntity<ResponseDto> generateQuestionsFallbackMethod(ExamRequestDto examRequestDto, Throwable throwable) {
        return new ResponseEntity<>(
                ResponseDto.builder()
                        .responseCode("")
                        .responseMessage("Oops! Generating questions request taking too long.")
                        .build(),
                HttpStatus.REQUEST_TIMEOUT);
    }

    @Override
    @CircuitBreaker(name = "populateQuestions", fallbackMethod = "populateQuestionsFallbackMethod")
    @Retry(name = "populateQuestions", fallbackMethod = "populateQuestionsFallbackMethod")
    public ExamResponseDto getQuestionsForExamSession(Long examId) {
        ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

//        log.info("size of the list is " + String.valueOf(examEntity.getQuestionId().size()));

        List<QuestionResponseDto> questionList = feignClient.getQuestionsBasedOnId(examEntity.getQuestionId()).getBody();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient("osasereu@gmail.com")
                .message("Hello, you just viewed a new exam session. Do take note of the session id: " + examEntity.getId() + ". Exam session title generated " + examEntity.getTitle())
                .subject("EXAM SESSION SUCCESSFULLY FETCHED")
                .build();
        rabbitMQProducer.sendViewQuizSessionEmailNotification(emailDetails);
        log.info(emailDetails + "viewed session sent just now");
//       return the exam i.e. the title and the actual questions associated with the question Ids stored using feign client
        return ExamResponseDto.builder()
                .id(examEntity.getId())
                .title(examEntity.getTitle())
                .listOfQuestions(questionList)
                .build();
    }
    @Override
    public ResponseEntity<ResponseDto> populateQuestionsFallbackMethod(Long examId, Throwable throwable) {
        return new ResponseEntity<>(
                ResponseDto.builder()
                        .responseCode("")
                        .responseMessage("Oops! Populating questions for exam instance taking too long.")
                        .build(),
                HttpStatus.REQUEST_TIMEOUT);
    }
    @Override
    @CircuitBreaker(name = "retrieveScore", fallbackMethod = "retrieveScoreFallbackMethod")
    @Retry(name = "retrieveScore", fallbackMethod = "retrieveScoreFallbackMethod")
    public Integer calculateScoreForExamSession(Long examId, List<AnswerResponseDto> answerResponseDto) {
        ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return feignClient.getScore(answerResponseDto).getBody();
    }

    @Override
    public ResponseEntity<ResponseDto> retrieveScoreFallbackMethod(Long examId, List<AnswerResponseDto> answerResponseDto, Throwable throwable) {
        return new ResponseEntity<>(
                ResponseDto.builder()
                        .responseCode("")
                        .responseMessage("Oops! Populating questions for exam instance taking too long.")
                        .build(),
                HttpStatus.REQUEST_TIMEOUT);
    }

    @Override
    public List<ExamResponseDto> fetchExamSession() {
        return examRepository.findAll().stream().map(examEntity -> ExamResponseDto.builder()
                .id(examEntity.getId())
                .title(examEntity.getTitle())
                .listOfQuestions(feignClient.getQuestionsBasedOnId(examEntity.getQuestionId()).getBody())
                .build()).collect(Collectors.toList());
    }
}
