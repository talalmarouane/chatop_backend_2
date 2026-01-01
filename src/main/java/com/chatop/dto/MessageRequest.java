/*
 * DTO représentant la structure d'une demande d'envoi de message.
 * Contient le corps du message, l'ID de l'utilisateur et l'ID de la location concernée.
 */
package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageRequest(
        String message,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("rental_id") Long rentalId) {
}
