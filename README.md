# Ejemplo Spring Boot MongoDB Reactivo

Proyecto de ejemplo que demuestra el uso de **Spring Boot** con **MongoDB** en modo reactivo, utilizando **WebFlux** y el driver reactivo de MongoDB.

## Tecnologías

| Tecnología                    | Versión                    |
|-------------------------------|----------------------------|
| Java                          | 25                         |
| Spring Boot                   | 4.0.6                      |
| Gradle                        | 9.4.1                      |
| Spring WebFlux                | gestionado por Spring Boot |
| Spring Data MongoDB Reactive  | gestionado por Spring Boot |
| Lombok                        | gestionado por Spring Boot |

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

| Dependencia                                  | Uso                                        |
|----------------------------------------------|--------------------------------------------|
| spring-boot-starter-webflux                  | Servidor HTTP reactivo con Project Reactor |
| spring-boot-starter-data-mongodb-reactive    | Acceso reactivo a MongoDB                  |
| spring-boot-starter-validation               | Validación de beans con Jakarta Validation |
| lombok                                       | Reducción de boilerplate                   |
| spring-boot-devtools                         | Recarga automática en desarrollo           |

## API REST — ejemplos con curl

La base URL es `http://localhost:8081/api/persons`.

### Obtener todas las personas

```bash
curl -s http://localhost:8081/api/persons | jq
```

### Obtener una persona por ID

```bash
curl -s http://localhost:8081/api/persons/$ID | jq
```

### Crear una persona (guarda el ID para el resto de llamadas)

```bash
ID=$(curl -s -X POST http://localhost:8081/api/persons \
  -H "Content-Type: application/json" \
  -d '{"name": "Carlos", "lastName": "Lopez"}' | jq -r '.id')
echo "ID creado: $ID"
```

### Actualizar una persona

```bash
curl -s -X PUT http://localhost:8081/api/persons/$ID \
  -H "Content-Type: application/json" \
  -d '{"name": "Carlos", "lastName": "Garcia"}' | jq
```

### Eliminar una persona

```bash
curl -s -X DELETE http://localhost:8081/api/persons/$ID | jq
```

### Buscar personas por nombre (con paginación)

```bash
curl -s "http://localhost:8081/api/persons/byName/Carlos?page=0&size=10" | jq
```

### Buscar personas por nombre ordenadas por apellido

```bash
curl -s "http://localhost:8081/api/persons/byNameResponseEntity/Carlos?page=0&size=10" | jq
```

> **Nota:** `name` debe tener entre 4 y 20 caracteres. `lastName` es obligatorio.
