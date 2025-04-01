package com.zhsaidk.exceptions;

public class ChatRoomNotCreated extends RuntimeException {
    public ChatRoomNotCreated(String message) {
        super(message);
    }
}
