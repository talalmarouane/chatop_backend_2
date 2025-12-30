package com.chatop.controller;

import com.chatop.dto.RentalDto;
import com.chatop.dto.RentalsResponse;
import com.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Endpoints for managing rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    @Operation(summary = "Get all rentals")
    public ResponseEntity<RentalsResponse> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a rental by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new rental")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture,
            Authentication authentication) {
        RentalDto rentalDto = new RentalDto(
                null,
                name,
                surface,
                price,
                null,
                description,
                null,
                null,
                null);

        rentalService.createRental(rentalDto, picture, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Rental created !"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update a rental")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {
        RentalDto rentalDto = new RentalDto(
                id,
                name,
                surface,
                price,
                null,
                description,
                null,
                null,
                null);
        rentalService.updateRental(id, rentalDto);
        return ResponseEntity.ok(Map.of("message", "Rental updated !"));
    }
}
