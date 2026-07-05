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
@Document(collection = "locations")
public class Location {

    @Id
    private String id;

    @NotBlank(message = "name is required")
    @Indexed
    private String name;

    @NotBlank(message = "type is required")
    @Indexed
    private String type; // Planet, Cluster, Space station...

    private String dimension;

    @Builder.Default
    private List<String> residentIds = new ArrayList<>(); // ref -> Character

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
