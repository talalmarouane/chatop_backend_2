# 📚 Guide Swagger/OpenAPI - Tag #1

**Date de création** : 2025-12-21  
**Sujet** : Intégration complète de Swagger/OpenAPI dans Spring Boot

---

## 🎯 Introduction

**Swagger** (maintenant appelé **OpenAPI**) est un outil qui :
- 📝 **Documente automatiquement** ton API REST
- 🧪 **Permet de tester** les endpoints directement dans le navigateur  
- 📄 **Génère une spécification JSON** de ton API

---

## 📦 Partie 1 : Installation

### Étape 1 : Ajouter la dépendance Maven

Dans `pom.xml` :

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

**💡 Versions :**
- ✅ `springdoc-openapi` pour Spring Boot 3.x
- ❌ `springfox` (obsolète)
- ❌ `springdoc 1.x` (Spring Boot 2.x seulement)

---

## ⚙️ Partie 2 : Configuration de base

### Créer `SwaggerConfig.java`

```java
package com.votreapp.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Nom de ton API",
        version = "1.0",
        description = "Description de ton API"
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Dev")
    }
)
public class SwaggerConfig {
    // Configuration de base - pas de code nécessaire
}
```

---

## 🔐 Partie 3 : Ajouter la sécurité JWT

### Mise à jour de `SwaggerConfig.java`

```java
package com.votreapp.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Nom de ton API",
        version = "1.0",
        description = "Description de ton API"
    )
)
@SecurityScheme(
    name = "bearerAuth",           // Nom du schéma (à utiliser dans @SecurityRequirement)
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {
}
```

**Résultat** : Bouton "Authorize 🔒" dans Swagger UI !

---

## 📝 Partie 4 : Documenter les endpoints

### Annotations sur les Controllers

```java
package com.votreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestion des utilisateurs")
public class UserController {

    @GetMapping
    @Operation(summary = "Liste tous les utilisateurs")
    public List<User> getAllUsers() {
        // ...
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID")
    @SecurityRequirement(name = "bearerAuth")  // JWT requis
    public User getUserById(@PathVariable Long id) {
        // ...
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur")
    public User createUser(@RequestBody UserDto dto) {
        // ...
    }
}
```

### Table des annotations

| Annotation | Utilité | Exemple |
|------------|---------|---------|
| `@Tag` | Groupe les endpoints | `@Tag(name = "Users")` |
| `@Operation` | Description de l'endpoint | `@Operation(summary = "Get all")` |
| `@SecurityRequirement` | Nécessite authentification | `@SecurityRequirement(name = "bearerAuth")` |
| `@Parameter` | Décrit un paramètre | `@Parameter(description = "User ID")` |

---

## 🔧 Partie 5 : Configuration Spring Security

**⚠️ IMPORTANT** : Autoriser Swagger dans Spring Security !

Dans `SecurityConfig.java` :

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",      // Interface Swagger
                "/v3/api-docs/**"      // JSON API docs
            )
            .permitAll()
            .anyRequest().authenticated()
        );
    return http.build();
}
```

---

## 🌐 Partie 6 : Accès à Swagger UI

### URLs importantes

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui/index.html` | Interface Swagger UI |
| `http://localhost:8080/v3/api-docs` | Documentation JSON |
| `http://localhost:8080/v3/api-docs.yaml` | Documentation YAML |

**Note** : Remplace `8080` par ton port configuré dans `application.properties`

---

## 🎨 Partie 7 : Personnalisation

### Dans `application.properties`

```properties
# Changer le chemin de Swagger UI
springdoc.swagger-ui.path=/doc

# Changer le chemin des API docs
springdoc.api-docs.path=/api-docs

# Trier les endpoints alphabétiquement
springdoc.swagger-ui.operationsSorter=alpha

# Trier les tags (groupes) alphabétiquement
springdoc.swagger-ui.tagsSorter=alpha

# Activer le mode "try it out" par défaut
springdoc.swagger-ui.tryItOutEnabled=true
```

---

## 📚 Partie 8 : Annotations avancées

### Documentation détaillée des réponses

```java
@Operation(
    summary = "Créer une location",
    description = "Créer une nouvelle annonce avec image",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Succès",
            content = @Content(
                schema = @Schema(implementation = MessageResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requête invalide"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié"
        )
    }
)
@PostMapping
public ResponseEntity<MessageResponse> create(@RequestBody CreateDto dto) {
    // ...
}
```

### Documentation des paramètres

```java
@GetMapping("/{id}")
@Operation(summary = "Get user by ID")
public User getUser(
    @Parameter(
        description = "ID de l'utilisateur",
        required = true,
        example = "123"
    )
    @PathVariable Long id
) {
    // ...
}
```

---

## ✅ Checklist d'intégration

Pour ton prochain projet :

- [ ] 1. Ajouter dépendance `springdoc-openapi-starter-webmvc-ui`
- [ ] 2. Créer `SwaggerConfig.java` avec `@OpenAPIDefinition`
- [ ] 3. Ajouter `@SecurityScheme` si JWT
- [ ] 4. Ajouter `@Tag` sur les controllers
- [ ] 5. Ajouter `@Operation` sur les méthodes
- [ ] 6. Ajouter `@SecurityRequirement` sur endpoints protégés
- [ ] 7. Autoriser `/swagger-ui/**` dans Spring Security
- [ ] 8. Tester sur `http://localhost:PORT/swagger-ui/index.html`

---

## 🐛 Problèmes courants

### 1. Page Swagger vide ou 404

**Solution** : Vérifier que Spring Security autorise les URLs :
```java
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
```

### 2. Bouton "Authorize" absent

**Solution** : Ajouter `@SecurityScheme` dans `SwaggerConfig.java`

### 3. Endpoints non visibles

**Solution** : Vérifier que le controller a `@RestController` et un `@RequestMapping`

---

## 📎 Ressources utiles

- [Documentation officielle springdoc](https://springdoc.org/)
- [Spécification OpenAPI 3.0](https://swagger.io/specification/)
- [Annotations Swagger](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)

---

**💾 Sauvegarde** : Tag #1 - Guide Swagger/OpenAPI  
**À revisiter** : Quand tu voudras intégrer Swagger dans un nouveau projet

---
