package com.zhsaidk.http.controller;

import com.zhsaidk.service.MessageService;
import com.zhsaidk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "chatId", required = false) String chatId, Model model){
        model.addAttribute("messages", messageService.getMessagesByChatId(chatId));
        return "chat/chat";
    }

    @GetMapping("/rooms")
    public String listChats(Principal principal, Model model){
        model.addAttribute("rooms", userService.findAllRooms(principal));
        return "chat/chats";
    }
}
