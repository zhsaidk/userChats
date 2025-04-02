package com.zhsaidk.handler;

import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomWebSocketHandler implements WebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Клиент подключился: {}", session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Получен сообщение {} {}", session.getId(), message.getPayload());
        if (message instanceof TextMessage textMessage){
            for (WebSocketSession socketSession : sessions) {
                if (socketSession.isOpen() && socketSession != session){
                    socketSession.sendMessage(new TextMessage(textMessage.getPayload()));
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Ошибка соединение {} {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        log.info("Клиент отключился: {} (Причина: {})", session.getId(), closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
