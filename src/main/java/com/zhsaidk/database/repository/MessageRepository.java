package com.zhsaidk.database.repository;

import com.zhsaidk.database.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findMessageByChatIdOrderByTimestamp(String chatId, Pageable pageable);
    List<Message> findMessageByChatIdOrderByTimestamp(String chatId); //todo Для рест запросов через Swagger, можно обеднить
}
