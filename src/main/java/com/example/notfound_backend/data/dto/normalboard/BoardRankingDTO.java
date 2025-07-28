package com.example.notfound_backend.data.dto.normalboard;

import java.math.BigInteger;
import java.time.Instant;


public interface BoardRankingDTO {
    Integer getId();
    String getTitle();
    String getAuthor();
    Integer getRecommend();
    Integer getViews();
    String getCategory();
    Instant getCreatedAt();
    BigInteger getCommentCount();
    String getAuthorNickname();
    String getGrade();
}
