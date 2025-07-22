package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.data.entity.BoardQnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardQnaRepository extends JpaRepository<BoardQnaEntity, Integer> {

    List<BoardQnaEntity> findAll();

    @Modifying
    @Query("UPDATE BoardQnaEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardQnaEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardQnaEntity b SET b.recommend = b.recommend - 1 WHERE b.id = :id AND b.recommend > 0")
    void decrementRecommend(@Param("id") Integer id);

    Optional<BoardQnaEntity> findById(Integer id);

    @Query(value = """
        SELECT 
            b.id AS id,
            b.title AS title,
            b.author AS author,
            b.recommend AS recommend,
        u.nickname AS authorNickname,
            b.views AS views,
            b.category AS category,
            b.created_at AS createdAt,
            (SELECT COUNT(*) 
             FROM board_qna_comments c 
             WHERE c.board_id = b.id AND c.status = 'VISIBLE') AS commentCount
        FROM board_qna b
                JOIN user_auth ua ON b.author = ua.username 
            JOIN user_info u ON ua.username = u.username 
        WHERE DATE(b.created_at) = CURRENT_DATE AND b.status = 'VISIBLE'
        ORDER BY commentCount DESC
        LIMIT 5
        """, nativeQuery = true)
    List<BoardRankingDTO> findTop5ByCommentsToday(); //댓글 top5

    @Query(value = """
    SELECT 
        b.id AS id,
        b.title AS title,
        b.author AS author,
        b.recommend AS recommend,
        b.views AS views,
        u.nickname AS authorNickname,
        b.category AS category,
        b.created_at AS createdAt,
        (SELECT COUNT(*) 
         FROM board_qna_comments c 
         WHERE c.board_id = b.id AND c.status = 'VISIBLE') AS commentCount
    FROM board_qna b
        JOIN user_auth ua ON b.author = ua.username 
            JOIN user_info u ON ua.username = u.username 
    WHERE DATE(b.created_at) = CURRENT_DATE AND b.status = 'VISIBLE'
    ORDER BY b.recommend DESC
    LIMIT 5
    """, nativeQuery = true)
    List<BoardRankingDTO> findTop5ByRecommendToday();

}
