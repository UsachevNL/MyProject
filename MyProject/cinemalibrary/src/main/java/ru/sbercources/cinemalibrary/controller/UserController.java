package ru.sbercources.cinemalibrary.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.LoginDto;
import ru.sbercources.cinemalibrary.dto.UserDto;
import ru.sbercources.cinemalibrary.mapper.GenericMapper;
import ru.sbercources.cinemalibrary.mapper.UserMapper;
import ru.sbercources.cinemalibrary.model.User;
import ru.sbercources.cinemalibrary.security.JwtTokenUtil;
import ru.sbercources.cinemalibrary.service.GenericService;
import ru.sbercources.cinemalibrary.service.UserService;
import ru.sbercources.cinemalibrary.service.userDetails.CustomUserDetailsService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rest/user")
public class UserController extends GenericController<User, UserDto> {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService service;
    private UserController(UserService service, UserMapper mapper, JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
        super(service, mapper);
        this.jwtTokenUtil = jwtTokenUtil;
        this.service = service;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody LoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();

        if(!service.checkPassword(loginDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!\nWrongPassword");
        }
        UserDetails foundUser = customUserDetailsService.loadUserByUsername(loginDto.getLogin());
        String token = jwtTokenUtil.generateToken(foundUser);
        response.put("token", token);
        response.put("authorities", foundUser.getAuthorities());
        return ResponseEntity.ok().body(response);

    }
}
