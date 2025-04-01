package com.zhsaidk.service;

import com.zhsaidk.database.entity.ChatRoom;
import com.zhsaidk.database.entity.User;
import com.zhsaidk.database.repository.ChatRoomRepository;
import com.zhsaidk.database.repository.UserRepository;
import com.zhsaidk.exceptions.ChatRoomNotCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void createChatRoom(String chatName, List<String> userIds){
        List<User> usersByIdIn = userRepository.findUsersByIdIn(userIds);
        if (userIds.size() != usersByIdIn.size()){
            throw new ChatRoomNotCreated("один или несколько участников не найдены");
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatName)
                .userIds(userIds)
                .build();

        chatRoomRepository.save(chatRoom);
        log.info("Чат был создан: {}", chatName);
    }

    public ChatRoom findChatRoomById(String id){
        return chatRoomRepository.findChatRoomById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ChatRoom> findChatRoomsByUserId(String id){
        return chatRoomRepository.findChatRoomsByUserIdsContaining(id);
    }

    public List<ChatRoom> findAll(){
        return chatRoomRepository.findAll();
    }
}
