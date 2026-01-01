/*
 * DTO contenant une liste de RentalDto.
 * Utilisé pour renvoyer la liste complète des locations au format JSON.
 */
package com.chatop.dto;

import java.util.List;

public record RentalsResponse(List<RentalDto> rentals) {
}
