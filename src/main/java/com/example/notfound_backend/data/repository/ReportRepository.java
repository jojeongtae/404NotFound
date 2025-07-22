package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

}
