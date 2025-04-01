package com.zhsaidk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class MessageDto {
    @NotNull(message = "Айди чата не должна быть пустым")
    String chatId;
    @NotBlank(message = "Сообщение не должно быть пустым")
    String text;
    List<String> replyToMessage; //Может быть null
}
