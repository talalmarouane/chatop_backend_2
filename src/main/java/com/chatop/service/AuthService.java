/*
 * Ce service contient la logique métier de l'authentification.
 * Il gère la création des utilisateurs, la vérification des identifiants
 * et la génération des tokens JWT.
 */
package com.chatop.service;

import com.chatop.dto.LoginRequest;
import com.chatop.dto.RegisterRequest;
import com.chatop.dto.TokenResponse;
import com.chatop.dto.UserDto;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /*
     * Objectif : Enregistrer un nouvel utilisateur en base de données.
     * Entrée : RegisterRequest (données d'inscription).
     * Sortie : TokenResponse (token JWT généré).
     */
    public TokenResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new TokenResponse(jwtToken);
    }

    /*
     * Objectif : Authentifier un utilisateur et générer un token.
     * Entrée : LoginRequest (login et mot de passe).
     * Sortie : TokenResponse (token JWT) ou erreur si échec.
     */
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.password()));
        var user = userRepository.findByEmail(request.login())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new TokenResponse(jwtToken);
    }

    /*
     * Objectif : Récupérer les détails d'un utilisateur par son email.
     * Entrée : Email de l'utilisateur.
     * Sortie : UserDto (détails) ou erreur si non trouvé.
     */
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
