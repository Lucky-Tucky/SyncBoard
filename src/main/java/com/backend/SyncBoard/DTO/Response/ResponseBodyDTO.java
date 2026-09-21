package com.backend.SyncBoard.DTO.Response;

import java.time.LocalDateTime;

public record ResponseBodyDTO (Object object , int status, LocalDateTime timestamp){
}
