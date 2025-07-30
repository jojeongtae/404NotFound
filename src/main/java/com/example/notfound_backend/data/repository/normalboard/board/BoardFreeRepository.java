package com.example.notfound_backend.data.repository.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardFreeRepository extends JpaRepository<BoardFreeEntity, Integer> {

    List<BoardFreeEntity> findAll();

    @Modifying
    @Query("UPDATE BoardFreeEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardFreeEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardFreeEntity b SET b.recommend = b.recommend - 1 WHERE b.id = :id AND b.recommend > 0")
    void decrementRecommend(@Param("id") Integer id);

    Optional<BoardFreeEntity> findById(Integer id);

    @Query("SELECT b FROM BoardFreeEntity b WHERE b.title LIKE %:keyword% AND b.status='VISIBLE'")
    List<BoardFreeEntity> findByTitle(@Param("keyword") String keyword);

    @Query(value = """
    SELECT b.*
    FROM board_free b
    JOIN user_auth ua ON b.author = ua.username
    JOIN user_info ui ON ua.username = ui.username
    WHERE ui.nickname LIKE %:nickname%
      AND b.status = 'VISIBLE'
    """, nativeQuery = true)
    List<BoardFreeEntity> findByAuthor(@Param("nickname") String nickname);

    @Query(value = """
        SELECT 
            b.id AS id,
            b.title AS title,
            b.author AS author,
        u.nickname AS authorNickname,
            b.recommend AS recommend,
            b.views AS views,
            b.category AS category,
            b.created_at AS createdAt,
            (SELECT COUNT(*) 
             FROM board_free_comments c 
             WHERE c.board_id = b.id AND c.status = 'VISIBLE') AS commentCount
        FROM board_free b
                JOIN user_auth ua ON b.author = ua.username 
            JOIN user_info u ON ua.username = u.username 
        WHERE DATE(b.created_at) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) AND CURRENT_DATE
                AND b.status = 'VISIBLE'
        ORDER BY commentCount DESC
        LIMIT 5
        """, nativeQuery = true)
    List<BoardRankingDTO> findTop5ByCommentsInLast7Days(); //댓글 top5

    @Query(value = """
    SELECT 
        b.id AS id,
        b.title AS title,
        b.author AS author,
        u.nickname AS authorNickname,
        b.recommend AS recommend,
        b.views AS views,
        b.category AS category,
        b.created_at AS createdAt,
        (SELECT COUNT(*) 
         FROM board_free_comments c 
         WHERE c.board_id = b.id AND c.status = 'VISIBLE') AS commentCount
    FROM board_free b
        JOIN user_auth ua ON b.author = ua.username 
            JOIN user_info u ON ua.username = u.username 
    WHERE DATE(b.created_at) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) AND CURRENT_DATE
        AND b.status = 'VISIBLE'
    ORDER BY b.recommend DESC
    LIMIT 5
    """, nativeQuery = true)
    List<BoardRankingDTO> findTop5ByRecommendInLast7Days();

    @Query(value = """
    SELECT\s
        b.id AS id,
        b.title AS title,
        b.author AS author,
        u.nickname AS authorNickname,
        b.recommend AS recommend,
        b.views AS views,
        b.category AS category,
        b.created_at AS createdAt
    FROM board_food b
    JOIN user_auth ua ON b.author = ua.username\s
    JOIN user_info u ON ua.username = u.username\s
    WHERE b.status = 'VISIBLE'
    ORDER BY b.recommend DESC
    LIMIT 5
    """, nativeQuery = true)
    List<BoardRankingDTO> findTop5ByRecommend();

}
