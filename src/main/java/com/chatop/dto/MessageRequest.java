package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageRequest(
        String message,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("rental_id") Long rentalId) {
}
