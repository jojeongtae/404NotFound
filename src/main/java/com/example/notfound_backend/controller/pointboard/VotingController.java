package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dto.pointboard.VotingDTO;
import com.example.notfound_backend.service.pointboard.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<VotingDTO> createVoting(@RequestPart("votingDTO") VotingDTO votingDTO,
                                                  @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        VotingDTO created=votingService.createVoting(votingDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VotingDTO> updateVoting(@PathVariable Integer id,
                                                  @RequestPart("votingDTO") VotingDTO votingDTO,
                                                  @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        VotingDTO updated=votingService.updateVoting(id, votingDTO, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VotingDTO> deleteVoting(@PathVariable Integer id) throws IOException {
        votingService.deleteVoting(id);
        return  ResponseEntity.noContent().build();
    }
}
