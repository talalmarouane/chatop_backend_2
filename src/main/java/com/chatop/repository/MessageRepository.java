/*
 * Interface Repository pour l'entité Message.
 * Étend JpaRepository pour bénéficier des méthodes CRUD standard.
 */
package com.chatop.repository;

import com.chatop.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
