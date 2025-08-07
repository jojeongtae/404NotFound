package com.example.notfound_backend.data.repository.admin;

import com.example.notfound_backend.data.entity.admin.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    @Query(value = "select r from ReportEntity r where  r.reporter.username = :reporter")
    List<ReportEntity> findByReporter(@Param("reporter") String reporter);
}
