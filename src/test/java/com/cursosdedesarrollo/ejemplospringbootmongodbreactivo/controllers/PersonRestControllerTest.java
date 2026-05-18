package com.cursosdedesarrollo.ejemplospringbootmongodbreactivo.controllers;

import com.cursosdedesarrollo.ejemplospringbootmongodbreactivo.domain.Person;
import com.cursosdedesarrollo.ejemplospringbootmongodbreactivo.repositories.ReactivePersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonRestControllerTest {

    @Mock
    private ReactivePersonRepository repository;

    @InjectMocks
    private PersonRestController controller;

    private WebTestClient client;

    private Person alice;
    private Person bob;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(controller).build();
        alice = new Person("1", "Alice", "Smith");
        bob   = new Person("2", "Bobby", "Jones");
    }

    // -------------------------------------------------------------------------
    // GET /api/persons
    // -------------------------------------------------------------------------

    @Test
    void getAllPersons_returnsAll() {
        when(repository.findAll()).thenReturn(Flux.just(alice, bob));

        client.get().uri("/api/persons")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(2)
                .contains(alice, bob);
    }

    @Test
    void getAllPersons_empty_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Flux.empty());

        client.get().uri("/api/persons")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(0);
    }

    // -------------------------------------------------------------------------
    // GET /api/persons/{id}
    // -------------------------------------------------------------------------

    @Test
    void getPersonById_found_returns200() {
        when(repository.findById("1")).thenReturn(Mono.just(alice));

        client.get().uri("/api/persons/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(alice);
    }

    @Test
    void getPersonById_notFound_returns404() {
        when(repository.findById("99")).thenReturn(Mono.empty());

        client.get().uri("/api/persons/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    // -------------------------------------------------------------------------
    // POST /api/persons
    // -------------------------------------------------------------------------

    @Test
    void addPerson_validBody_returns200() {
        Person newPerson = new Person(null, "Carlos", "Lopez");
        Person saved     = new Person("3", "Carlos", "Lopez");
        when(repository.save(any(Person.class))).thenReturn(Mono.just(saved));

        client.post().uri("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newPerson)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(saved);
    }

    @Test
    void addPerson_saveFails_returns404() {
        Person newPerson = new Person(null, "Carlos", "Lopez");
        when(repository.save(any(Person.class))).thenReturn(Mono.empty());

        client.post().uri("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newPerson)
                .exchange()
                .expectStatus().isNotFound();
    }

    // -------------------------------------------------------------------------
    // PUT /api/persons/{id}
    // -------------------------------------------------------------------------

    @Test
    void updatePersonById_found_returns200() {
        Person update  = new Person(null, "Alice", "Updated");
        Person updated = new Person("1", "Alice", "Updated");

        when(repository.findById("1")).thenReturn(Mono.just(alice));
        when(repository.save(any(Person.class))).thenReturn(Mono.just(updated));

        client.put().uri("/api/persons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(updated);
    }

    @Test
    void updatePersonById_notFound_returns404() {
        Person update = new Person(null, "Alice", "Updated");
        when(repository.findById("99")).thenReturn(Mono.empty());

        client.put().uri("/api/persons/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .exchange()
                .expectStatus().isNotFound();
    }

    // -------------------------------------------------------------------------
    // DELETE /api/persons/{id}
    // -------------------------------------------------------------------------

    @Test
    void deletePerson_found_returns200() {
        when(repository.findById("1")).thenReturn(Mono.just(alice));
        when(repository.delete(alice)).thenReturn(Mono.empty());

        client.delete().uri("/api/persons/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(alice);
    }

    @Test
    void deletePerson_notFound_returns404() {
        when(repository.findById("99")).thenReturn(Mono.empty());

        client.delete().uri("/api/persons/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    // -------------------------------------------------------------------------
    // GET /api/persons/byName/{name}
    // -------------------------------------------------------------------------

    @Test
    void getByName_returnsMatchingPersons() {
        when(repository.findByName(eq("Alice"), any(PageRequest.class)))
                .thenReturn(Flux.just(alice));

        client.get().uri("/api/persons/byName/Alice?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(1)
                .contains(alice);
    }

    @Test
    void getByName_noResults_returnsEmptyList() {
        when(repository.findByName(eq("Desconocido"), any(PageRequest.class)))
                .thenReturn(Flux.empty());

        client.get().uri("/api/persons/byName/Desconocido")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(0);
    }

    // -------------------------------------------------------------------------
    // GET /api/persons/byNameResponseEntity/{name}
    // -------------------------------------------------------------------------

    @Test
    void getByNameOrdered_returnsPersonsOrderedByLastName() {
        when(repository.findByNameOrderByLastName(eq("Alice"), any(PageRequest.class)))
                .thenReturn(Flux.just(alice));

        client.get().uri("/api/persons/byNameResponseEntity/Alice?page=0&size=5")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(1)
                .contains(alice);
    }
}
