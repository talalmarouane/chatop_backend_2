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

    public void updateRental(Long id, RentalDto rentalDto) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setName(rentalDto.name());
        rental.setSurface(rentalDto.surface());
        rental.setPrice(rentalDto.price());
        rental.setDescription(rentalDto.description());

        rentalRepository.save(rental);
    }

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
