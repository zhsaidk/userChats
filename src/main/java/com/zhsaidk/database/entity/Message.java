package com.zhsaidk.database.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "message")
public class Message {
    @Id
    private String id;
    @NotNull
    private String chatId;
    @NotNull
    private String senderId;
    @NotNull
    private String text;
    private List<String> replyToMessageId;
    @NotNull
    private long timestamp;
}
