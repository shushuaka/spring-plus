package org.example.expert.domain.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomErrorResponse {
    private int code;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}