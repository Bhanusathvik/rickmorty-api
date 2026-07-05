package com.rickmorty.api.controller;

import com.rickmorty.api.dto.PagedResponse;
import com.rickmorty.api.model.Episode;
import com.rickmorty.api.service.EpisodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @PostMapping
    public ResponseEntity<Episode> create(@Valid @RequestBody Episode episode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(episodeService.create(episode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Episode> getById(@PathVariable String id) {
        return ResponseEntity.ok(episodeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Episode> update(@PathVariable String id, @Valid @RequestBody Episode episode) {
        return ResponseEntity.ok(episodeService.update(id, episode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        episodeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/episodes?name=pilot&episodeCode=S01E01&sortBy=airDate&direction=asc&page=0&size=20
     */
    @GetMapping
    public ResponseEntity<PagedResponse<Episode>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String episodeCode,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var result = episodeService.search(name, episodeCode, sortBy, direction, page, size);
        return ResponseEntity.ok(PagedResponse.from(result));
    }
}
