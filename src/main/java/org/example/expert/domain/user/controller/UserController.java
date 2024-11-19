package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@Auth AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

    @PatchMapping("/users/{userId}/nickname")
    public ResponseEntity<String> updateNickname(
            @PathVariable Long userId,
            @RequestBody String newNickname
    ) {
        userService.updateNickname(userId, newNickname);
        UserResponse user = userService.getUser(userId);
        // 닉네임 업데이트 후 새로운 JWT 반환
        String newToken = jwtUtil.createToken(user.getId(), user.getEmail(), newNickname, user.getUserRole());
        return ResponseEntity.ok(newToken);
    }
}
