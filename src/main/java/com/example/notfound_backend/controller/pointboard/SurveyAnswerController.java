package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dto.pointboard.SurveyAnswerDTO;
import com.example.notfound_backend.service.pointboard.SurveyAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey-answers")
public class SurveyAnswerController {

    private final SurveyAnswerService surveyAnswerService;

    @PostMapping("/new")
    public ResponseEntity<SurveyAnswerDTO> submitAnswer(@RequestBody SurveyAnswerDTO dto) {
        return ResponseEntity.ok(surveyAnswerService.submitAnswer(dto));
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<List<SurveyAnswerDTO>> getAnswersBySurvey(@PathVariable Integer surveyId) {
        return ResponseEntity.ok(surveyAnswerService.getAnswersBySurvey(surveyId));
    }
}
