/*
 * Ce contrôleur gère la récupération des informations utilisateur.
 * Il permet d'obtenir les détails d'un utilisateur spécifique via son ID.
 */
package com.chatop.controller;

import com.chatop.dto.UserDto;
import com.chatop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Objectif : Lire les informations publiques d'un utilisateur.
     * Entrée : ID de l'utilisateur (via l'URL).
     * Sortie : UserDto (nom, email, etc.).
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
