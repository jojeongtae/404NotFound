package com.example.notfound_backend.data.repository.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.normalboard.board.BoardInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardInfoRepository extends JpaRepository<BoardInfoEntity, Integer> {

    List<BoardInfoEntity> findAll();

    @Modifying
    @Query("UPDATE BoardInfoEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardInfoEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardInfoEntity b SET b.recommend = b.recommend - 1 WHERE b.id = :id AND b.recommend > 0")
    void decrementRecommend(@Param("id") Integer id);

    Optional<BoardInfoEntity> findById(Integer id);

    @Query("SELECT b FROM BoardInfoEntity b WHERE b.title LIKE %:keyword% AND b.status='VISIBLE'")
    List<BoardInfoEntity> findByTitle(@Param("keyword") String keyword);

    @Query(value = """
    SELECT b.*
    FROM board_info b
    JOIN user_auth ua ON b.author = ua.username
    JOIN user_info ui ON ua.username = ui.username
    WHERE ui.nickname LIKE %:nickname%
      AND b.status = 'VISIBLE'
    """, nativeQuery = true)
    List<BoardInfoEntity> findByAuthor(@Param("nickname") String nickname);

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
             FROM board_info_comments c 
             WHERE c.board_id = b.id AND c.status = 'VISIBLE') AS commentCount
        FROM board_info b
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
        u.nickname AS authorNickname,
        b.views AS views,
        b.category AS category,
        b.created_at AS createdAt,
        (SELECT COUNT(*) 
         FROM board_info_comments c 
         WHERE c.board_id = b.id AND c.status = 'VISIBLE') AS commentCount
    FROM board_info b
        JOIN user_auth ua ON b.author = ua.username 
            JOIN user_info u ON ua.username = u.username 
    WHERE DATE(b.created_at) = CURRENT_DATE AND b.status = 'VISIBLE'
    ORDER BY b.recommend DESC
    LIMIT 5
    """, nativeQuery = true)
    List<BoardRankingDTO> findTop5ByRecommendToday();

}
