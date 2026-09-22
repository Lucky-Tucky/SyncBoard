package com.backend.SyncBoard.DTO.Response;

import java.time.LocalDateTime;

public record ResponseBodyDTO (Object data , int status, LocalDateTime timestamp){
}
