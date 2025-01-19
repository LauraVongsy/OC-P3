# Chatop

## Description

**Chatop** est un projet utilisant **Spring Boot**, c'est un portail de mise en relation entre les futurs locataires et
les propriétaires
pour de la location saisonnière sur la côte basque dans un premier temps et, plus tard, sur toute la France..
Ce projet illustre comment construire une application web basée sur Spring, intégrant des fonctionnalités telles que
l'accès aux données,
la sécurité, et l'intégration avec une base de données MySQL.

## Prérequis

Avant de commencer, assurez-vous d'avoir les outils suivants installés sur votre machine :

- **Java 21** ou une version ultérieure
- **Maven 3.8+**
- **MySQL**

## Fonctionnalités

Le projet intègre les fonctionnalités suivantes :

- **Spring Boot Starter Web** : Développement d'API REST.
- **Spring Boot Starter Data JPA** : Accès aux données via JPA/Hibernate.
- **Spring Boot Starter Security** : Sécurisation des endpoints.
- **Springdoc OpenAPI** : Documentation de l'API avec Swagger UI.
- **JWT** : Gestion des tokens pour l'authentification.
- **Lombok** : Réduction du boilerplate dans le code.
- **MySQL Connector** : Connexion à une base de données MySQL.

## Installation

1. Clonez le projet depuis le dépôt Git :

   ```bash
   git clone https://github.com/LauraVongsy/OC-P3.git
   cd chatop

2. Configurez la base de données dans le fichier application.properties:
   Il est fortement conseillé de stocker les informations de connexion à la base de données dans des variables
   d'environnement.

spring.datasource.url=jdbc:mysql://localhost:3306/chatop
spring.datasource.username=your_username
spring.datasource.password=your_password

3. Compilez le projet:

```mvn clean install```

4. Lancez l'application:

```mvn spring-boot: run```

## Documentation de l'API

Une documentation interactive de l'API est disponible avec OpenAPI.
Une fois le projet lancé, accédez à :

http://localhost:3001/swagger-ui/index.html

## Création de la base de données

Le script sql pour créer la base de données se trouve dans: ressources/sql/script.sql




