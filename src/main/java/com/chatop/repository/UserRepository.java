/*
 * Interface Repository pour l'entité User.
 * Contient une méthode spécifique pour trouver un utilisateur par son email.
 */
package com.chatop.repository;

import com.chatop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
