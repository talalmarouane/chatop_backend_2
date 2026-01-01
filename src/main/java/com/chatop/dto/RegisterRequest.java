/*
 * DTO (Data Transfer Object) utilisé pour l'inscription d'un nouvel utilisateur.
 * Contient l'email, le nom et le mot de passe.
 */
package com.chatop.dto;

public record RegisterRequest(String email, String name, String password) {
}
