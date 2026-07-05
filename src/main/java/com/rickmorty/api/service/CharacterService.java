package com.rickmorty.api.service;

import com.rickmorty.api.exception.ApiExceptions.ResourceNotFoundException;
import com.rickmorty.api.model.Character;
import com.rickmorty.api.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final MongoTemplate mongoTemplate;

    public Character create(Character character) {
        character.setId(null);
        return characterRepository.save(character);
    }

    public Character getById(String id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Character", id));
    }

    public Character update(String id, Character updated) {
        Character existing = getById(id);
        existing.setName(updated.getName());
        existing.setStatus(updated.getStatus());
        existing.setSpecies(updated.getSpecies());
        existing.setType(updated.getType());
        existing.setGender(updated.getGender());
        existing.setOriginName(updated.getOriginName());
        existing.setLocationId(updated.getLocationId());
        existing.setLocationName(updated.getLocationName());
        existing.setImage(updated.getImage());
        existing.setEpisodeIds(updated.getEpisodeIds());
        return characterRepository.save(existing);
    }

    public void delete(String id) {
        if (!characterRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Character", id);
        }
        characterRepository.deleteById(id);
    }


    public Page<Character> search(String name, String status, String species,String sortBy, String direction, int page, int size) {
        Query query = new Query();
        List<Criteria> criteriaList = new java.util.ArrayList<>();

        if (name != null && !name.isBlank()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }
        if (status != null && !status.isBlank()) {
            criteriaList.add(Criteria.where("status").regex("^" + status + "$", "i"));
        }
        if (species != null && !species.isBlank()) {
            criteriaList.add(Criteria.where("species").regex("^" + species + "$", "i"));
        }
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Character.class);

        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = (sortBy == null || sortBy.isBlank()) ? "name" : sortBy;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));

        query.with(pageable);
        List<Character> results = mongoTemplate.find(query, Character.class);

        return new org.springframework.data.domain.PageImpl<>(results, pageable, total);
    }
}
