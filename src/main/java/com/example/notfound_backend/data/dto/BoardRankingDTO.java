package com.example.notfound_backend.data.dto;

import lombok.*;

import java.time.Instant;


public interface BoardRankingDTO {
    Integer getId();
    String getTitle();
    String getAuthor();
    Integer getRecommend();
    Integer getViews();
    String getCategory();
    Instant getCreatedAt();
    Integer getCommentCount();


}
