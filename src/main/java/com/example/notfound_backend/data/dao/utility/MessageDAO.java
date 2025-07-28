package com.example.notfound_backend.data.dao.utility;

import com.example.notfound_backend.data.entity.utility.MessageEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.repository.utility.MessageRepository;
import com.example.notfound_backend.data.repository.login.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageDAO {

    private final MessageRepository messageRepository;
    private final UserAuthRepository userAuthRepository;

    public MessageEntity save(MessageEntity messageEntity) {
        return messageRepository.save(messageEntity);
    }

    public Optional<MessageEntity> findById(Integer id) {
        return messageRepository.findById(id);
    }

    public List<MessageEntity> findByAuthor(String author) {
        UserAuthEntity Author=userAuthRepository.findByUsername(author);
        return messageRepository.findByAuthor(Author);
    }

    public List<MessageEntity> findByReceiver(String receiver) {
        UserAuthEntity Receiver=userAuthRepository.findByUsername(receiver);
        return messageRepository.findByReceiver(Receiver);
    }

    public void delete(MessageEntity messageEntity) {
        messageRepository.delete(messageEntity);
    }

}
