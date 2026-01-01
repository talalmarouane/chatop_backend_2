/*
 * Ce service gère la récupération des données utilisateur.
 * Il fait le lien avec le repository pour trouver un utilisateur par son ID.
 */
package com.chatop.service;

import com.chatop.dto.UserDto;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /*
     * Objectif : Trouver un utilisateur par son ID.
     * Entrée : ID de l'utilisateur.
     * Sortie : UserDto.
     */
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
