package com.easytest.examsession.service;

import com.easytest.examsession.dto.ExamRequestDto;
import com.easytest.examsession.dto.ExamResponseDto;
import com.easytest.examsession.dto.ResponseDto;
import com.easytest.examsession.dto.communicaton.AnswerResponseDto;
import com.easytest.examsession.repository.ExamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExamService implements ExamServiceInterface{

    private ExamRepository examRepository;

    @Override
    public ResponseDto createQuizSession(ExamRequestDto examRequestDto) {
        return null;
    }

    @Override
    public ExamResponseDto fetchExamSessionById(Long examId) {
        return null;
    }

    @Override
    public Integer getScoreForExam(Long examId, AnswerResponseDto answerResponseDto) {
        return null;
    }
}
