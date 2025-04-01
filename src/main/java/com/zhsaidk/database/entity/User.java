package com.zhsaidk.database.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.Session;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    @Email(message = "Не корректный email")
    private String email;
    @NotBlank(message = "Имя не должен быть пустым")
    private String firstName;
    @NotBlank(message = "Фамилия не должен быть пустым")
    private String lastName;
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;
    private Role role;
}