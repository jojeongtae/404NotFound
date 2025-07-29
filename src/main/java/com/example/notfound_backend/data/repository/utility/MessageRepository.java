package com.example.notfound_backend.data.repository.utility;

import com.example.notfound_backend.data.entity.utility.MessageEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Integer> {

    List<MessageEntity> findByAuthor(UserAuthEntity author);

    List<MessageEntity> findByReceiver(UserAuthEntity receiver);

}
