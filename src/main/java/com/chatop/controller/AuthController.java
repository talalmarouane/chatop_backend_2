/*
 * Ce contrôleur gère l'authentification des utilisateurs.
 * Il expose les points de terminaison pour l'inscription (/register), la connexion (/login)
 * et la récupération des informations de l'utilisateur courant (/me).
 */
package com.chatop.controller;

import com.chatop.dto.LoginRequest;
import com.chatop.dto.RegisterRequest;
import com.chatop.dto.TokenResponse;
import com.chatop.dto.UserDto;
import com.chatop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
     * Objectif : Inscrire un nouvel utilisateur dans l'application.
     * Entrée : RegisterRequest (contient email, nom, mot de passe).
     * Sortie : TokenResponse (le token JWT pour l'authentification).
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token.")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /*
     * Objectif : Authentifier un utilisateur existant.
     * Entrée : LoginRequest (login/email, mot de passe).
     * Sortie : TokenResponse (le token JWT).
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token.")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /*
     * Objectif : Récupérer les informations de l'utilisateur connecté.
     * Entrée : Authentication (injecté par Spring Security via le token).
     * Sortie : UserDto (détails de l'utilisateur : nom, email, dates).
     */
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get current user", description = "Returns the details of the currently authenticated user.")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authService.getCurrentUser(authentication.getName()));
    }
}
