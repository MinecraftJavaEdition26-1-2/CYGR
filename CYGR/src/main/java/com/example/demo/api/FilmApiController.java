package com.example.demo.api;

import com.example.demo.repository.FilmRepository;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FilmRequest;
import com.example.demo.dto.FilmResponse;
import com.example.demo.entity.Film;
import com.example.demo.service.FilmService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/films")
public class FilmApiController {
    private final FilmRepository filmRepository;
    private final FilmService filmService;

    public FilmApiController(FilmService filmService, FilmRepository filmRepository) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
    }

    @GetMapping //GET /api/v1/films?recherche=minecraft
    public List<FilmResponse> lister(@RequestParam(required = false) String recherche){
        return filmService.rechercherParNom(recherche).stream().map(FilmResponse::from).toList();
    }

    @GetMapping("{id}")// GET /api/v1/films/1
    public FilmResponse lire(@PathVariable Long id) {
        return FilmResponse.from(filmService.findById(id));
    }
    
    @PutMapping("/{id}")
    public FilmResponse modifier(@PathVariable Long id, @Valid @RequestBody FilmRequest request) {
        return FilmResponse.from(filmService.modifier(id, request.toEntity()));
    }

    @PostMapping // POST /api/v1/films
    public ResponseEntity<FilmResponse> creer(@Valid @RequestBody FilmRequest request) {
        Film film = filmRepository.save(request.toEntity());
        return ResponseEntity.created(URI.create("/api/v1/films/" + film.getId())).body(FilmResponse.from(film));
    }

    @DeleteMapping("/{id}") // DELETE /api/v1/films/1
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        filmService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
