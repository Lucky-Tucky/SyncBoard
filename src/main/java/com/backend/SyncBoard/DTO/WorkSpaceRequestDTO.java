package com.backend.SyncBoard.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record WorkSpaceRequestDTO (
        @NotBlank
        @Size(min = 3)
        String name,

        List<String> tags,

        @NotBlank
        @Size(min = 3)
        String key){
}
