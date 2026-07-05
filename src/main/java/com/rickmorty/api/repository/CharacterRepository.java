package com.rickmorty.api.repository;

import com.rickmorty.api.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
}
