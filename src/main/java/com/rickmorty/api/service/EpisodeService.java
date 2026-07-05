package com.rickmorty.api.service;

import com.rickmorty.api.exception.ApiExceptions.ResourceNotFoundException;
import com.rickmorty.api.model.Episode;
import com.rickmorty.api.repository.EpisodeRepository;
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
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final MongoTemplate mongoTemplate;

    public Episode create(Episode episode) {
        episode.setId(null);
        return episodeRepository.save(episode);
    }

    public Episode getById(String id) {
        return episodeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Episode", id));
    }

    public Episode update(String id, Episode updated) {
        Episode existing = getById(id);
        existing.setName(updated.getName());
        existing.setAirDate(updated.getAirDate());
        existing.setEpisodeCode(updated.getEpisodeCode());
        existing.setCharacterIds(updated.getCharacterIds());
        return episodeRepository.save(existing);
    }

    public void delete(String id) {
        if (!episodeRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Episode", id);
        }
        episodeRepository.deleteById(id);
    }

    public Page<Episode> search(String name, String episodeCode,
                                 String sortBy, String direction, int page, int size) {

        Query query = new Query();
        List<Criteria> criteriaList = new java.util.ArrayList<>();

        if (name != null && !name.isBlank()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }
        if (episodeCode != null && !episodeCode.isBlank()) {
            criteriaList.add(Criteria.where("episodeCode").regex("^" + episodeCode + "$", "i"));
        }
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Episode.class);

        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = (sortBy == null || sortBy.isBlank()) ? "name" : sortBy;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));

        query.with(pageable);
        List<Episode> results = mongoTemplate.find(query, Episode.class);

        return new PageImpl<>(results, pageable, total);
    }
}
