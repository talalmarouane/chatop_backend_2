/*
 * Gestionnaire global des exceptions.
 * Capture les erreurs survenant dans l'application pour renvoyer des réponses HTTP appropriées.
 */
package com.chatop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Gestionnaire global des exceptions.
 * Permet de renvoyer des messages d'erreur clairs à l'utilisateur.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Gère les erreurs d'exécution génériques (RuntimeException).
     * Renvoie une réponse 400 Bad Request avec le message d'erreur.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        System.out.println("❌ Erreur RuntimeException interceptée : " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    /*
     * Gère les erreurs d'authentification (BadCredentialsException).
     * Renvoie une réponse 401 Unauthorized avec un message d'erreur générique.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "error"));
    }

    /*
     * Gère toutes les autres exceptions non interceptées (Exception).
     * Renvoie une réponse 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "An error occurred: " + ex.getMessage()));
    }
}
