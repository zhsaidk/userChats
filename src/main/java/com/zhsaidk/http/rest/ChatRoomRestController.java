package com.zhsaidk.http.rest;

import com.zhsaidk.database.entity.ChatRoom;
import com.zhsaidk.database.entity.User;
import com.zhsaidk.database.repository.ChatRoomRepository;
import com.zhsaidk.database.repository.UserRepository;
import com.zhsaidk.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController("/api/v3/chat")
@RequiredArgsConstructor
public class ChatRoomRestController {
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(String name, String ... ids){
        if (name != null && ids.length != 0){
            chatRoomService.createChatRoom(name, List.of(ids));
            return ResponseEntity.status(HttpStatus.OK).build();
        };
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/findAll")
    public List<ChatRoom> findAll(){
        return chatRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id){
        return ResponseEntity.ok(chatRoomService.findChatRoomById(id));
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMember(String chatId, String userId){

        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Чат не существует"));

        if (chatRoom.getUserIds().contains(userId)){
            log.warn("Пользователь уже есть в группе");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        List<String> userIds = chatRoom.getUserIds();
        userIds.add(userId);
        chatRoom.setUserIds(userIds);
        chatRoomRepository.save(chatRoom);
        log.info("Пользователь {} был добавлен в чат {}", userId, chatId);
        return ResponseEntity.ok(chatRoom);
    }
}
