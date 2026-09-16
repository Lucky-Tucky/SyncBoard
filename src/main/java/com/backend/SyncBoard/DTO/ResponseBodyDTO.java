package com.backend.SyncBoard.DTO;

import java.time.LocalDateTime;

public record ResponseBodyDTO (Object object , int status, LocalDateTime timestamp){
}
