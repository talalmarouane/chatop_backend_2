/*
 * Configuration du bean ModelMapper.
 * Permet d'injecter une instance de ModelMapper dans les services pour la conversion Entity/DTO.
 */
package com.chatop.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
