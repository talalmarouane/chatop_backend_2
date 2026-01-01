/*
 * DTO (Data Transfer Object) représentant les données envoyées lors d'une demande de connexion.
 * Contient l'email et le mot de passe de l'utilisateur.
 */
package com.chatop.dto;

public record LoginRequest(String login, String password) {
}
