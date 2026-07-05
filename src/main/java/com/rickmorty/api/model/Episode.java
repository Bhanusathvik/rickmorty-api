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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "episodes")
public class Episode {

    @Id
    private String id;

    @NotBlank(message = "name is required")
    @Indexed
    private String name;

    @NotBlank(message = "airDate is required")
    private String airDate; // e.g. "December 2, 2013"

    @NotBlank(message = "episodeCode is required")
    @Indexed
    private String episodeCode; // e.g. "S01E01"

    @Builder.Default
    private List<String> characterIds = new ArrayList<>(); // ref -> Character

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
