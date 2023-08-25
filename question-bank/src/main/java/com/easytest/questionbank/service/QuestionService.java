package com.easytest.questionbank.service;

import com.easytest.questionbank.dto.QuestionRequestDto;
import com.easytest.questionbank.dto.QuestionResponseDto;
import com.easytest.questionbank.dto.ResponseDto;
import com.easytest.questionbank.dto.communication.AnswerResponseDto;
import com.easytest.questionbank.entity.QuestionEntity;
import com.easytest.questionbank.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionService implements QuestionServiceInterface {
    private ModelMapper modelMapper;

    private QuestionRepository questionRepository;

    @Override
    public List<QuestionResponseDto> getAllQuestions() {
        return questionRepository.findAll().stream().map(question ->
                QuestionResponseDto.builder()
                        .title(question.getTitle())
                        .option1(question.getOption1())
                        .option2(question.getOption2())
                        .option3(question.getOption3())
                        .option4(question.getOption4())
                        .build()
        ).collect(Collectors.toList());
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
    public List<Long> generateQuestionsForQuiz(String category, Integer numberOfQuestions) {
        List<Long> listOfQuestions = questionRepository.findByExamCategory(category).stream().map(QuestionEntity::getId).collect(Collectors.toList());
        Collections.shuffle(listOfQuestions);
        return listOfQuestions.subList(0, Math.min(numberOfQuestions, listOfQuestions.size()));
    }

    @Override
    public QuestionResponseDto getQuestionById(Long id) {
        QuestionEntity questionEntity = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        QuestionResponseDto questionResponseDto = modelMapper.map(questionEntity, QuestionResponseDto.class);
        return questionResponseDto;
    }

    @Override
    public List<QuestionResponseDto> getQuestionsBasedOnId(List<Long> questionIds) {
        return questionIds.stream().map(id -> questionRepository.findById(id).get()).map(question -> QuestionResponseDto.builder()
                .title(question.getTitle())
                .option1(question.getOption1())
                .option2(question.getOption2())
                .option3(question.getOption3())
                .option4(question.getOption4())
                .build()).collect(Collectors.toList());

        //        List<QuestionEntity> questionEntities = new ArrayList<>();
//        for (Long id : questionIds){
//            questionEntities.add(questionRepository.findById(id).get());
//        }

        //    questionEntities.stream().map(question ->
//                QuestionResponseDto.builder()
//                        .title(question.getTitle())
//                        .option1(question.getOption1())
//                        .option2(question.getOption2())
//                        .option3(question.getOption3())
//                        .option4(question.getOption4())
//                        .build()).collect(Collectors.toList());

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
