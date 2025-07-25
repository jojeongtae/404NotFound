package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.VotingAnswerDTO;
import com.example.notfound_backend.service.VotingAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voting-answers")
public class VotingAnswerController {

    private final VotingAnswerService votingAnswerService;

    @PostMapping("/new")
    public ResponseEntity<VotingAnswerDTO> submitAnswer(@RequestBody VotingAnswerDTO votingAnswerDTO) {
        return ResponseEntity.ok(votingAnswerService.submitAnswer(votingAnswerDTO));
    }

    @GetMapping("/{votingId}")
    public ResponseEntity<List<VotingAnswerDTO>> getAnswersByVotingId(@PathVariable Integer votingId) {
        return ResponseEntity.ok(votingAnswerService.getAnswersByVoting(votingId));

    }
}
