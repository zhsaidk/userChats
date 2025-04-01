package com.zhsaidk.http.rest;

import com.zhsaidk.database.entity.Message;
import com.zhsaidk.database.entity.User;
import com.zhsaidk.database.repository.UserRepository;
import com.zhsaidk.dto.MessageDto;
import com.zhsaidk.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody @Valid MessageDto messageDto,
                                                 BindingResult bindingResult,
                                                 Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        if(!bindingResult.hasErrors()){
            Message buildMessage = Message.builder()
                    .chatId(messageDto.getChatId())
                    .senderId(user.getId())
                    .text(messageDto.getText())
                    .replyToMessageId(messageDto.getReplyToMessage())
                    .timestamp(System.currentTimeMillis())
                    .build();
            Message savedMessage = messageService.saveMessage(buildMessage);
            return ResponseEntity.ok(savedMessage);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Message>> getMessagesByRoomId(@PathVariable("roomId") String roomId) {
        List<Message> messages = messageService.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }
}