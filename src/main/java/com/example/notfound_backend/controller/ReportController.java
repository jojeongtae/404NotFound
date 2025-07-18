package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.ReportAddDTO;
import com.example.notfound_backend.data.dto.ReportResponseDTO;
import com.example.notfound_backend.data.dto.ReportUpdateByAdnimDTO;
import com.example.notfound_backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ReportController {
    private final ReportService reportService;

    // 신고
    @PostMapping(value = "/user/report")
    public ResponseEntity<ReportResponseDTO> addReport(@RequestBody ReportAddDTO reportAddDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.addReport(reportAddDTO));
    }

    // 신고리스트 (관리자)
    @GetMapping(value = "admin/report-list")
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getAllReports());
    }

    // 신고응답 (관리자)
    @PatchMapping(value = "/admin/report")
    public ResponseEntity<ReportResponseDTO> updateReportByAdmin(@RequestBody ReportUpdateByAdnimDTO reportUpdateByAdnimDTO) {
        ReportResponseDTO response = reportService.updateReportByAdmin(reportUpdateByAdnimDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
