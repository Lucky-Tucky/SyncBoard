package com.backend.SyncBoard.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Password not found")
        @Size(max = 20, min = 4 , message = "UserName not Valid")
        String password,

        @NotBlank(message = "Email not found")
        @Size(min = 4 , message = "UserName not Valid")
        String email) {
}
