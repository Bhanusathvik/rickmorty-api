package com.rickmorty.api.controller;

import com.rickmorty.api.dto.PagedResponse;
import com.rickmorty.api.model.Location;
import com.rickmorty.api.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> create(@Valid @RequestBody Location location) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.create(location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable String id) {
        return ResponseEntity.ok(locationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable String id, @Valid @RequestBody Location location) {
        return ResponseEntity.ok(locationService.update(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/locations?name=earth&type=planet&sortBy=name&direction=asc&page=0&size=20
     */
    @GetMapping
    public ResponseEntity<PagedResponse<Location>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var result = locationService.search(name, type, sortBy, direction, page, size);
        return ResponseEntity.ok(PagedResponse.from(result));
    }
}
