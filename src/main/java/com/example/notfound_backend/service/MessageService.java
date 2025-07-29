package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.MessageDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.MessageDTO;
import com.example.notfound_backend.data.entity.MessageEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.data.entity.UserStatus;
import com.example.notfound_backend.exception.UserSuspendedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageDAO messageDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

    public MessageDTO findById(Integer id){
        MessageEntity dtoList= messageDAO.findById(id)
                .orElseThrow();
        return toDTO(dtoList);
    }

    public List<MessageEntity> findMessageByAuthor(String username) {
        UserAuthEntity author = userAuthDAO.findByUsername(username);

        if (author == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        String authorName = author.getUsername();
        return messageDAO.findByAuthor(authorName);
    }

    public List<MessageEntity> findMessageByReceiver(String username) {
        UserAuthEntity receiver = userAuthDAO.findByUsername(username);

        if (receiver == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        String receiverName = receiver.getUsername();
        return messageDAO.findByReceiver(receiverName);
    }

    public MessageDTO sendMessage(MessageDTO messageDTO) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(messageDTO.getAuthor());
        if (userInfoEntity == null) {
            throw new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다: " + messageDTO.getAuthor());
        }
        UserStatus userStatus = userInfoEntity.getStatus();
        if (userStatus!=UserStatus.ACTIVE){
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }

        MessageEntity entity=new MessageEntity();
        entity.setTitle(messageDTO.getTitle());
        entity.setMessage(messageDTO.getMessage());

        UserAuthEntity author=userAuthDAO.findByUsername(messageDTO.getAuthor());
        entity.setAuthor(author);
        UserAuthEntity receiver=userAuthDAO.findByUsername(messageDTO.getReceiver());
        entity.setReceiver(receiver);

        entity.setCreatedAt(Instant.now());
        MessageEntity saved=messageDAO.save(entity);
        return toDTO(saved);
    }

    public MessageDTO toDTO(MessageEntity entity){

        String authorUsername = entity.getAuthor().getUsername();
        String receiverUsername = entity.getReceiver().getUsername();

        UserInfoEntity authorInfoEntity=userInfoDAO.getUserInfo(authorUsername);
        String authorNickname=authorInfoEntity.getNickname();

        UserInfoEntity receiverInfoEntity=userInfoDAO.getUserInfo(receiverUsername);
        String receiverNickname=receiverInfoEntity.getNickname();

        return new MessageDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getMessage(),
                authorUsername,
                authorNickname,
                receiverUsername,
                receiverNickname,
                entity.getCreatedAt()
        );
    }

    public void deleteMessage(Integer id) {
        MessageEntity entity=messageDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Message not found."));
        messageDAO.delete(entity);
    }
}
