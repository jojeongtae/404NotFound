package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dto.pointboard.SurveyDTO;
import com.example.notfound_backend.service.pointboard.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<SurveyDTO> create(@RequestPart("surveyDTO") SurveyDTO surveyDTO,
                                            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        SurveyDTO created = surveyService.createBoard(surveyDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDTO> update(@PathVariable Integer id,
                                            @RequestPart("surveyDTO") SurveyDTO surveyDTO,
                                            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        SurveyDTO updated = surveyService.updateBoard(id, surveyDTO, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
        surveyService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

}
