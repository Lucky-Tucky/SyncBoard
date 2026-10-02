package com.backend.SyncBoard.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectDTO(
        @NotBlank(message = "Project name cannot be blank")
        @NotNull(message = "Project name cannot be null")
        @Size(min = 3, max = 12, message = "Project name must be between {min} and {max} characters")
        String name,

        @NotBlank(message = "Project key cannot be blank")
        @NotNull(message = "Project key cannot be null")
        @Size(min = 3, max = 12, message = "Project key must be between {min} and {max} characters")
        String key,

        @NotBlank(message = "Workspace ID is required")
        @NotNull(message = "Workspace ID cannot be null")
        String workspaceId) {
}
