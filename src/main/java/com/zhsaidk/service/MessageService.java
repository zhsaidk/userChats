package com.zhsaidk.service;

import com.zhsaidk.database.entity.Message;
import com.zhsaidk.database.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(String roomId) {
        return messageRepository.findMessageByChatIdOrderByTimestamp(roomId);
    }
}
