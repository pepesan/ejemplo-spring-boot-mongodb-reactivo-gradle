package com.cursosdedesarrollo.ejemplospringbootmongodbreactivo.controllers;


import com.cursosdedesarrollo.ejemplospringbootmongodbreactivo.domain.Person;
import com.cursosdedesarrollo.ejemplospringbootmongodbreactivo.repositories.ReactivePersonRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Slf4j
@RestController
@RequestMapping("/api/persons")
public class PersonRestController {

    private ReactivePersonRepository reactiveMongoRepository;

    @Autowired
    public PersonRestController(ReactivePersonRepository reactiveMongoRepository){
        this.reactiveMongoRepository = reactiveMongoRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Flux<Person> getAllPerson() {
        return reactiveMongoRepository.findAll();
    }

    @PostMapping
    private Mono<ResponseEntity<Person>> addPerson(@Valid @RequestBody Person person) {
        return reactiveMongoRepository.save(person)
                .map(data -> ResponseEntity.ok(data))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Person>> getPersonById(
            @PathVariable(value = "id") String id) {
        return reactiveMongoRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Person>> updatePersonById(
            @PathVariable(value = "id") String id,
            @Valid @RequestBody Person person) {
        return reactiveMongoRepository.findById(id)
                .flatMap(existingPerson -> {
                    existingPerson.setName(person.getName());
                    existingPerson.setLastName(person.getLastName());
                    return reactiveMongoRepository.save(existingPerson);
                })
                .map(updatedPerson -> new ResponseEntity<>(updatedPerson, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Person>> deletePerson(
            @PathVariable(value = "id") String id) {

        return reactiveMongoRepository.findById(id)
                .flatMap(person ->
                        reactiveMongoRepository.delete(person)
                                .then(
                                        Mono.just(
                                                new ResponseEntity<Person>(person, HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/byName/{name}")
    private Flux<Person> getAllPersons(
            @PathVariable(value = "name") String name,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return reactiveMongoRepository.findByName(name, PageRequest.of(page, size));
    }

    @GetMapping("/byNameResponseEntity/{name}")
    @ResponseStatus(HttpStatus.OK)
    private Flux<Person> getAllPersonsResponseEntity(
            @PathVariable(value = "name") String name,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return reactiveMongoRepository.findByNameOrderByLastName(name, PageRequest.of(page,size));
    }

}
