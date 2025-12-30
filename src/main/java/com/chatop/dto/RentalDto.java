package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;

public record RentalDto(
        Long id,
        String name,
        BigDecimal surface,
        BigDecimal price,
        String picture,
        String description,
        @JsonProperty("owner_id") Long ownerId,
        @JsonProperty("created_at") Timestamp createdAt,
        @JsonProperty("updated_at") Timestamp updatedAt) {
}
