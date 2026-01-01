/*
 * Interface Repository pour l'entité Rental.
 * Permet d'interagir avec la base de données pour gérer les locations.
 */
package com.chatop.repository;

import com.chatop.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
