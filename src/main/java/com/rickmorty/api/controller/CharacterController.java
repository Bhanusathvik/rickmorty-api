package com.rickmorty.api.controller;

import com.rickmorty.api.dto.PagedResponse;
import com.rickmorty.api.model.Character;
import com.rickmorty.api.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping
    public ResponseEntity<Character> create(@Valid @RequestBody Character character) {
        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.create(character));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Character> getById(@PathVariable String id) {
        return ResponseEntity.ok(characterService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Character> update(@PathVariable String id, @Valid @RequestBody Character character) {
        return ResponseEntity.ok(characterService.update(id, character));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        characterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/characters?name=rick&status=alive&species=human&sortBy=name&direction=asc&page=0&size=20
     */
    @GetMapping
    public ResponseEntity<PagedResponse<Character>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String species,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var result = characterService.search(name, status, species, sortBy, direction, page, size);
        return ResponseEntity.ok(PagedResponse.from(result));
    }
}
