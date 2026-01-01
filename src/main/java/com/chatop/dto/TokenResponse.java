/*
 * DTO contenant le token JWT généré.
 * Renvoyé au client après une authentification réussie.
 */
package com.chatop.dto;

public record TokenResponse(String token) {
}
