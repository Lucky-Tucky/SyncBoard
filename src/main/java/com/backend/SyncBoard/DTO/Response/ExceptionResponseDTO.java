package com.backend.SyncBoard.DTO.Response;
import java.time.LocalDateTime;

public record ExceptionResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {

}