package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.data.entity.BoardNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardNoticeRepository extends JpaRepository<BoardNoticeEntity, Integer> {
    List<BoardNoticeEntity> findAll();

    @Modifying
    @Query("UPDATE BoardNoticeEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardNoticeEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardNoticeEntity b SET b.recommend = b.recommend - 1 WHERE b.id = :id AND b.recommend > 0")
    void decrementRecommend(@Param("id") Integer id);

    Optional<BoardNoticeEntity> findById(Integer id);

    @Query("SELECT b FROM BoardNoticeEntity b WHERE b.title LIKE %:keyword% AND b.status='VISIBLE'")
    List<BoardNoticeEntity> findByTitle(@Param("keyword") String keyword);

    @Query(value = """
    SELECT b.*
    FROM board_notice b
    JOIN user_auth ua ON b.author = ua.username
    JOIN user_info ui ON ua.username = ui.username
    WHERE ui.nickname LIKE %:nickname%
      AND b.status = 'VISIBLE'
    """, nativeQuery = true)
    List<BoardNoticeEntity> findByAuthor(@Param("nickname") String nickname);

}
