package com.backend.SyncBoard.DTO.Response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record WorkSpaceResponseDTO(String name , String key , int issue_number , LocalDateTime createdAt, List<String> tags) {
}
