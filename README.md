# ChâTop API - Back End

Bienvenue sur le backend de l'application ChâTop. Ce projet est une API REST robuste développée avec Spring Boot, gérant l'authentification, les messages, les locations et les utilisateurs.

## 🛠️ Stack Technique

Ce projet repose sur une stack moderne :

-   **Langage** : Java 21
-   **Framework** : Spring Boot 3.3.0
-   **Base de données** : MySQL (Connecteur `mysql-connector-j`)
-   **Sécurité** : Spring Security avec JWT (JSON Web Tokens) pour l'authentification stateless.
-   **Documentation API** : SpringDoc OpenAPI (Swagger UI).
-   **Mapping** : ModelMapper pour la conversion Entité <-> DTO.
-   **Outil de Build** : Maven.

## 📋 Prérequis

Avant de commencer, assurez-vous de disposer de l'environnement suivant :

1.  **Java JDK 21** installé.
2.  **Maven** installé et configuré dans votre PATH.
3.  **MySQL Server** installé et en cours d'exécution.

## ⚙️ Configuration Rapide

La configuration se trouve dans `src/main/resources/application.properties`.

### Base de données
Assurez-vous que votre serveur MySQL est accessible.
Par défaut, l'application cherche une base nommée **`chatop`** sur le port 3306.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatop?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

⚠️ **Important** : Si votre mot de passe root est différent, modifiez la ligne `spring.datasource.password`.

## 🚀 Commencer (How to Run)

L'application tourne par défaut sur le port **3001**.

### Option 1 : Lancement Automatique (Recommandé)

Un script PowerShell intelligent est fourni (`restart.ps1`). Il vérifie si le port 3001 est occupé, tue le processus bloquant si nécessaire, et lance l'application proprement.

Dans un terminal PowerShell à la racine du dossier workspace :

```powershell
./restart.ps1
```

### Option 2 : Lancement Manuel (Maven)

Si vous préférez la méthode classique ou si vous êtes sur un autre OS :

1.  **Nettoyer et compiler** :
    ```bash
    mvn clean install
    ```
2.  **Lancer l'application** :
    ```bash
    mvn spring-boot:run
    ```

## 📚 Documentation API (Swagger)

Une fois le serveur démarré, la documentation complète des endpoints est accessible via Swagger UI :

👉 **[http://localhost:3001/swagger-ui/index.html](http://localhost:3001/swagger-ui/index.html)**

Vous pourrez y tester directement les routes :
-   `/api/auth/register` & `/login`
-   `/api/rentals`
-   `/api/messages`
-   `/api/user`

## 📂 Structure Clé

-   **`src/main/java`** : Contient le code source (Controllers, Services, Repositories, DTOs).
-   **`src/main/resources`** : Configuration (`application.properties`) et SQL d'initialisation si présent.
-   **`restart.ps1`** : Script utilitaire pour le redémarrage rapide.
