package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record UserDto(
        Long id,
        String name,
        String email,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt) {
}
