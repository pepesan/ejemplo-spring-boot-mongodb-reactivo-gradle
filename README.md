# Ejemplo Spring Boot MongoDB Reactivo

Proyecto de ejemplo que demuestra el uso de **Spring Boot** con **MongoDB** en modo reactivo, utilizando **WebFlux** y el driver reactivo de MongoDB.

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.6 |
| Gradle | 9.4.1 |
| Spring WebFlux | (gestionado por Spring Boot) |
| Spring Data MongoDB Reactive | (gestionado por Spring Boot) |
| Lombok | (gestionado por Spring Boot) |

## Requisitos previos

- JDK 17 o superior
- MongoDB en ejecución (por defecto en `localhost:27017`)
- Gradle 9.4.1 (o usar el wrapper incluido)

## Arrancar el proyecto

```bash
./gradlew bootRun
```

La aplicación arranca en el puerto **8081**: `http://localhost:8081`

## Compilar y generar el JAR

```bash
./gradlew build
```

El artefacto generado se encuentra en `build/libs/`.

## Ejecutar los tests

```bash
./gradlew test
```

## Configuración

La configuración se encuentra en `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: ejemplo-spring-boot-mongodb-reactivo

server:
  port: 8081
```

Para cambiar la URL de conexión a MongoDB, añade en el YAML:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/nombre_base_datos
```

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/cursosdedesarrollo/ejemplospringbootmongodbreactivo/
│   │   └── EjemploSpringBootMongodbReactivoApplication.java
│   └── resources/
│       └── application.yaml
└── test/
    └── java/com/cursosdedesarrollo/ejemplospringbootmongodbreactivo/
        └── EjemploSpringBootMongodbReactivoApplicationTests.java
```

## Dependencias principales

- **spring-boot-starter-webflux** — servidor HTTP reactivo con Project Reactor
- **spring-boot-starter-data-mongodb-reactive** — acceso reactivo a MongoDB
- **spring-boot-starter-validation** — validación de beans con Jakarta Validation
- **lombok** — reducción de boilerplate (getters, setters, constructores)
- **spring-boot-devtools** — recarga automática en desarrollo
