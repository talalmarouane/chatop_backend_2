package com.chatop.controller;

import com.chatop.dto.MessageRequest;
import com.chatop.dto.MessageResponse;
import com.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Endpoints for messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(summary = "Send a message")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest request) {
        messageService.createMessage(request);
        return ResponseEntity.ok(new MessageResponse("Message send with success"));
    }
}
