package com.zhsaidk.http.rest;

import com.zhsaidk.database.entity.ChatRoom;
import com.zhsaidk.database.entity.User;
import com.zhsaidk.dto.UserReadDto;
import com.zhsaidk.service.ChatRoomService;
import com.zhsaidk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v3/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<List<UserReadDto>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user,
                                    @Validated BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            UserReadDto userReadDto = userService.save(user);
            log.info("User сохранен успешно!");
            return ResponseEntity.ok().body(userReadDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}/chats")
    public ResponseEntity<List<ChatRoom>> findChatRoomByUserId(@PathVariable("id") String id){
        if(id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(chatRoomService.findChatRoomsByUserId(id));
    }
}
