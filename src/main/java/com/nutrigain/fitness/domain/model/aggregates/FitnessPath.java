package com.nutrigain.fitness.domain.model.aggregates;

import com.nutrigain.fitness.domain.model.valueobjects.PathType;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "fitness_paths")
public class FitnessPath extends AggregateRoot {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private PathType type;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(length = 300)
    private String imageUrl;

    protected FitnessPath() {
    }

    public FitnessPath(PathType type, String name, String description, String imageUrl) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public PathType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
