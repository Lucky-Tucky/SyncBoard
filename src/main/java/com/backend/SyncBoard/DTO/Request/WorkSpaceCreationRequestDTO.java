package com.backend.SyncBoard.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record WorkSpaceCreationRequestDTO(
        @NotBlank
        @Size(min = 3)
        String name,

        List<String> tags,

        @NotBlank
        @Size(min = 3)
        String key){
}
