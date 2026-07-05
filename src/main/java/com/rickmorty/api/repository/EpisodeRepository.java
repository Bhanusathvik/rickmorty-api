package com.rickmorty.api.repository;

import com.rickmorty.api.model.Episode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends MongoRepository<Episode, String> {
}
