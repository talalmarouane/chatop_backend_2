/*
 * Ce service contient la logique métier pour les locations.
 * Il gère la conversion entre Entités et DTOs, la sauvegarde des images sur le disque
 * et l'interaction avec la base de données pour les locations.
 */
package com.chatop.service;

import com.chatop.dto.RentalDto;
import com.chatop.dto.RentalsResponse;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.servlet.context-path:@null}")
    private String contextPath;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /*
     * Objectif : Récupérer la liste complète des locations.
     * Entrée : Aucune.
     * Sortie : RentalsResponse contenant la liste des RentalDto.
     */
    public RentalsResponse getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<RentalDto> rentalDtos = rentals.stream()
                .map(rental -> new RentalDto(
                        rental.getId(),
                        rental.getName(),
                        rental.getSurface(),
                        rental.getPrice(),
                        rental.getPicture(),
                        rental.getDescription(),
                        rental.getOwner().getId(),
                        rental.getCreatedAt(),
                        rental.getUpdatedAt()))
                .collect(Collectors.toList());
        return new RentalsResponse(rentalDtos);
    }

    /*
     * Objectif : Récupérer une location par son ID.
     * Entrée : ID de la location.
     * Sortie : RentalDto (ou erreur si non trouvée).
     */
    public RentalDto getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));
        return new RentalDto(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt(),
                rental.getUpdatedAt());
    }

    /*
     * Objectif : Créer une nouvelle location avec image.
     * Entrée : RentalDto (données), MultipartFile (image), email du propriétaire.
     * Sortie : Aucune (void).
     */
    public void createRental(RentalDto rentalDto, MultipartFile picture, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String pictureUrl = saveFile(picture);

        Rental rental = new Rental();
        rental.setName(rentalDto.name());
        rental.setSurface(rentalDto.surface());
        rental.setPrice(rentalDto.price());
        rental.setDescription(rentalDto.description());
        rental.setPicture(pictureUrl);
        rental.setOwner(owner);

        rentalRepository.save(rental);
    }

    /*
     * Objectif : Mettre à jour les informations d'une location.
     * Entrée : ID de la location, RentalDto (nouvelles données).
     * Sortie : Aucune (void).
     */
    public void updateRental(Long id, RentalDto rentalDto) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setName(rentalDto.name());
        rental.setSurface(rentalDto.surface());
        rental.setPrice(rentalDto.price());
        rental.setDescription(rentalDto.description());

        rentalRepository.save(rental);
    }

    /*
     * Objectif : Sauvegarder un fichier image sur le système de fichiers local.
     * Entrée : MultipartFile (le fichier uploadé).
     * Sortie : String (URL d'accès à l'image).
     */
    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                String baseUrl = "http://localhost:" + serverPort;
                return baseUrl + "/uploads/" + fileName;
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }
}
