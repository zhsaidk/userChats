package com.zhsaidk.database.repository;

import com.zhsaidk.database.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findChatRoomsById(String id);

    List<ChatRoom> findChatRoomsByUserIdsContaining(String userId);

    Optional<ChatRoom> findChatRoomById(String id);
}
