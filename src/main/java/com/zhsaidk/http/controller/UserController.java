package com.zhsaidk.http.controller;

import com.zhsaidk.database.entity.User;
import com.zhsaidk.database.repository.UserRepository;
import com.zhsaidk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String findAll(Model model){
        model.addAttribute("users", userService.findAll());
        return "users/users";
    }
}
