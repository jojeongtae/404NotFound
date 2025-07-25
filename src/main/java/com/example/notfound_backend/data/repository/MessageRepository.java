package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.MessageEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity,Integer> {

    List<MessageEntity> findByAuthor(UserAuthEntity author);

    List<MessageEntity> findByReceiver(UserAuthEntity receiver);

}
