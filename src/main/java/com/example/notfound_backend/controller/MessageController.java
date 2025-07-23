package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.MessageDTO;
import com.example.notfound_backend.data.entity.MessageEntity;
import com.example.notfound_backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> send(@RequestBody MessageDTO messageDTO) {
        MessageDTO created = messageService.sendMessage(messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

// 테스트용
//    @GetMapping("/author/{username}")
//    public ResponseEntity<List<MessageDTO>> findMessagesByAuthor(@PathVariable String username) {
//        List<MessageEntity> messages = messageService.findMessageByAuthor(username);
//        List<MessageDTO> dtoList = messages.stream()
//                .map(messageService::toDTO)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(dtoList);
//    }
//
//    @GetMapping("/receiver/{username}")
//    public ResponseEntity<List<MessageDTO>> findMessagesByReceiver(@PathVariable String username) {
//        List<MessageEntity> messages = messageService.findMessageByAuthor(username);
//        List<MessageDTO> dtoList = messages.stream()
//                .map(messageService::toDTO)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(dtoList);
//    }

    @GetMapping("/author")
    public ResponseEntity<List<MessageDTO>> findMessageByAuthor(Principal principal) {
        String username = principal.getName();  // 로그인한 사용자 이름
        List<MessageEntity> messages = messageService.findMessageByAuthor(username);
        List<MessageDTO> dtoList = messages.stream()
                .map(messageService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/receiver")
    public ResponseEntity<List<MessageDTO>> findMessageByReceiver(Principal principal) {
        String username = principal.getName();  // 로그인한 사용자 이름
        List<MessageEntity> messages = messageService.findMessageByReceiver(username);
        List<MessageDTO> dtoList = messages.stream()
                .map(messageService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<MessageDTO>> deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

}
