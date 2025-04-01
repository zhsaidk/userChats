package com.zhsaidk.mapper;

import com.zhsaidk.database.entity.User;
import com.zhsaidk.dto.UserReadDto;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto map(User from) {
        return new UserReadDto(
                from.getId(),
                from.getEmail(),
                from.getFirstName(),
                from.getLastName());
    }
}
