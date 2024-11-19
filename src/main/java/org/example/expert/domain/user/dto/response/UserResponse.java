package org.example.expert.domain.user.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserRole userRole;

    public UserResponse(Long id, String email, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
