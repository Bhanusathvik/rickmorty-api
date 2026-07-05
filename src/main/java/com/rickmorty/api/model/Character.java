package com.rickmorty.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Doubles as both the persisted document and the request/response body. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "characters")
public class Character {

    @Id
    private String id;

    @NotBlank(message = "name is required")
    @Indexed
    private String name;

    @NotBlank(message = "status is required")
    @Indexed
    private String status; // Alive, Dead, unknown

    @NotBlank(message = "species is required")
    @Indexed
    private String species;

    private String type;
    private String gender; // Female, Male, Genderless, unknown
    private String originName;

    private String locationId;   // ref -> Location
    private String locationName;
    private String image;

    @Builder.Default
    private List<String> episodeIds = new ArrayList<>(); // ref -> Episode

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
