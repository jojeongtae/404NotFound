package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dto.pointboard.SurveyDTO;
import com.example.notfound_backend.service.pointboard.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/list")
    public List<SurveyDTO> getAllBoards() {
        List<SurveyDTO> surveyDTOList = surveyService.findAll();
        System.out.println(surveyDTOList.size());
        return surveyDTOList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getBoard(@PathVariable Integer id){
        SurveyDTO dto= surveyService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<SurveyDTO> create(@RequestBody SurveyDTO surveyDTO) {
        SurveyDTO created = surveyService.createBoard(surveyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDTO> update(@PathVariable Integer id, @RequestBody SurveyDTO surveyDTO) {
        SurveyDTO updated = surveyService.updateBoard(id, surveyDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        surveyService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

}
