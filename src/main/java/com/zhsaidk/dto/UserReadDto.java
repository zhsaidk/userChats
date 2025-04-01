package com.zhsaidk.dto;

import lombok.Value;

@Value
public class UserReadDto {
    String id;
    String email;
    String firstName;
    String lastName;
}
