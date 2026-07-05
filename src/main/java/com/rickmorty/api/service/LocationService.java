package com.rickmorty.api.service;

import com.rickmorty.api.exception.ApiExceptions.ResourceNotFoundException;
import com.rickmorty.api.model.Location;
import com.rickmorty.api.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final MongoTemplate mongoTemplate;

    public Location create(Location location) {
        location.setId(null);
        return locationRepository.save(location);
    }

    public Location getById(String id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Location", id));
    }

    public Location update(String id, Location updated) {
        Location existing = getById(id);
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setDimension(updated.getDimension());
        existing.setResidentIds(updated.getResidentIds());
        return locationRepository.save(existing);
    }

    public void delete(String id) {
        if (!locationRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Location", id);
        }
        locationRepository.deleteById(id);
    }

    public Page<Location> search(String name, String type,
                                  String sortBy, String direction, int page, int size) {

        Query query = new Query();
        List<Criteria> criteriaList = new java.util.ArrayList<>();

        if (name != null && !name.isBlank()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }
        if (type != null && !type.isBlank()) {
            criteriaList.add(Criteria.where("type").regex("^" + type + "$", "i"));
        }
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Location.class);

        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = (sortBy == null || sortBy.isBlank()) ? "name" : sortBy;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));

        query.with(pageable);
        List<Location> results = mongoTemplate.find(query, Location.class);

        return new PageImpl<>(results, pageable, total);
    }
}
