package com.zhsaidk.handler;

import com.zhsaidk.database.entity.Message;
import com.zhsaidk.database.entity.User;
import com.zhsaidk.database.repository.MessageRepository;
import com.zhsaidk.database.repository.UserRepository;
import com.zhsaidk.service.MessageService;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.*;

import java.sql.Array;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomWebSocketHandler implements WebSocketHandler {
    private final Map<String, List<WebSocketSession>> sessions = new HashMap<>();
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatId = Objects.requireNonNull(session.getUri()).getQuery().split("=")[1];
        if (chatId == null || chatId.isEmpty()){
            log.info("chatId не указан, закрываем соединение {}", session.getId());
            session.close();
            return;
        }
        sessions.computeIfAbsent(chatId, k-> new ArrayList<>()).add(session);
        log.info("Клиент подключился к комнате {}: {} ", chatId, session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String chatId = Objects.requireNonNull(session.getUri()).getQuery().split("=")[1];
        log.info("Получен сообщение в комнате {} от {} : {}", chatId, session.getId(), message.getPayload());

        User user = userRepository.findUserByEmail(session.getPrincipal().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Message messageBuilder = Message.builder()
                .chatId(chatId)
                .senderId(user.getId())
                .text((String) message.getPayload())
                .timestamp(System.currentTimeMillis())
                .build();
        messageService.saveMessage(messageBuilder);

        List<WebSocketSession> sessionList = sessions.getOrDefault(chatId, new ArrayList<>());
        for (WebSocketSession socketSession : sessionList) {
            if (socketSession.isOpen() && socketSession != session){
                socketSession.sendMessage(new TextMessage((String) message.getPayload()));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Ошибка соединений {} {} ", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String chatId = Objects.requireNonNull(session.getUri()).getQuery().split("=")[1];
        List<WebSocketSession> sessionList = sessions.get(chatId);
        if (sessionList != null){
            sessionList.remove(session);
            if (sessionList.isEmpty()){
                sessions.remove(chatId);
            }
        }
        log.info("Клиент {} отключился от комнаты {}. (Причина: {})", session.getId(), chatId, closeStatus.getReason());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
