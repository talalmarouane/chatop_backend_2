package com.chatop.service;

import com.chatop.dto.MessageRequest;
import com.chatop.model.Message;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.MessageRepository;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

        private final MessageRepository messageRepository;
        private final UserRepository userRepository;
        private final RentalRepository rentalRepository;

        public MessageService(MessageRepository messageRepository, UserRepository userRepository,
                        RentalRepository rentalRepository) {
                this.messageRepository = messageRepository;
                this.userRepository = userRepository;
                this.rentalRepository = rentalRepository;
        }

        public void createMessage(MessageRequest request) {
                User user = userRepository.findById(request.userId())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Rental rental = rentalRepository.findById(request.rentalId())
                                .orElseThrow(() -> new RuntimeException("Rental not found"));

                Message message = new Message();
                message.setUser(user);
                message.setRental(rental);
                message.setMessage(request.message());

                messageRepository.save(message);
        }
}
