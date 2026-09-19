package com.backend.SyncBoard.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record AuthBodyDTO(String username,
                          @JsonIgnore
                          String refreshToken,
                          String token,
                          int code,
                          LocalDateTime timeStamp) {
}
