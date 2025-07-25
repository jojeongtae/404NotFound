package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.VotingDTO;
import com.example.notfound_backend.data.entity.VotingEntity;
import com.example.notfound_backend.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voting")
public class VotingController {
    private final VotingService votingService;

    @GetMapping("/list")
    public List<VotingDTO> getVoting() {
        List<VotingDTO> votingDTOList=votingService.findAll();
        return votingDTOList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingDTO> getVotingById(@PathVariable Integer id) {
        VotingDTO dto=votingService.viewVoting(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<VotingDTO> createVoting(@RequestBody VotingDTO dto) {
        VotingDTO created=votingService.createVoting(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VotingDTO> updateVoting(@PathVariable Integer id, @RequestBody VotingDTO dto) {
        VotingDTO updated=votingService.updateVoting(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VotingDTO> deleteVoting(@PathVariable Integer id) {
        votingService.deleteVoting(id);
        return  ResponseEntity.noContent().build();
    }
}
