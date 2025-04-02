package com.zhsaidk.service;

import com.zhsaidk.config.EncoderPassword;
import com.zhsaidk.database.entity.ChatRoom;
import com.zhsaidk.database.entity.Role;
import com.zhsaidk.database.entity.User;
import com.zhsaidk.database.repository.ChatRoomRepository;
import com.zhsaidk.database.repository.UserRepository;
import com.zhsaidk.dto.UserReadDto;
import com.zhsaidk.mapper.UserReadMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserReadMapper userReadMapper;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping
    public List<UserReadDto> findAll(){
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    @PostConstruct
    public void init_db(){

        String adminEmail = "zhavokhir02@gmail.com";
        if (userRepository.findUserByEmail(adminEmail).isEmpty()){
            User user = User.builder()
                    .email(adminEmail)
                    .firstName("Zhavokhir")
                    .lastName("Saidkulov")
                    .password(passwordEncoder.encode("123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(user);
            log.info("Данные инициализированы, метод init_db вызван");
        }
        else{
            log.info("Данные уже существует");
        }
    }

    public UserReadDto findById(String id){
        return userRepository.findById(id)
                .map(userReadMapper::map)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    public UserReadDto save(User user){
        return Optional.of(userRepository.save(user))
                .map(userReadMapper::map)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public List<ChatRoom> findAllRooms(Principal principal){
        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return chatRoomRepository.findChatRoomsByUserIdsContaining(user.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .map(user -> {
                    return new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            Set.of(user.getRole())
                    );
                })
                .orElseThrow(()->new UsernameNotFoundException("USER NOT FOUND"));
    }
}
