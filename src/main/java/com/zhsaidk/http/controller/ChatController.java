package com.zhsaidk.http.controller;

import com.zhsaidk.database.entity.Message;
import com.zhsaidk.service.MessageService;
import com.zhsaidk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/chat/messages")
    @ResponseBody
    public List<Message> getMessages(@RequestParam(value = "chatId", required = false) String chatId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        ArrayList<Message> messages = new ArrayList<>(messageService.getMessagesByChatId(chatId, pageable).getContent());
        Collections.reverse(messages);
        return messages;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "chatId", required = false) String chatId,
                       @RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       Model model) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        ArrayList<Message> messages = new ArrayList<>(messageService.getMessagesByChatId(chatId, pageable).getContent());
        Collections.reverse(messages);
        model.addAttribute("messages", messages);
        model.addAttribute("chatId", chatId);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "chat/chat";
    }

    @GetMapping("/rooms")
    public String listChats(Principal principal, Model model){
        model.addAttribute("rooms", userService.findAllRooms(principal));
        return "chat/chats";
    }
}
