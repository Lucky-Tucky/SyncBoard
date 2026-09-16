package com.backend.SyncBoard.DTO;

import java.time.LocalDateTime;

public record AuthBodyDTO(String username, String token, int code, LocalDateTime timeStamp) {
}
