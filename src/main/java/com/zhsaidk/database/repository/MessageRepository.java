package com.zhsaidk.database.repository;

import com.zhsaidk.database.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findMessageByChatIdOrderByTimestamp(String chatId);
}
