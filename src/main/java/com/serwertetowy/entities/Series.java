package com.serwertetowy.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Series {
    @Id
    @SequenceGenerator(name = "series_id_sequence", sequenceName = "series_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_id_sequence")
    private Long id;
    private String name;
    private String description;
    @JsonManagedReference
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private Set<SeriesTags> seriesTags;
    @OneToMany(mappedBy = "series")
    private Set<Rating> seriesRatings;
    @OneToMany(mappedBy = "series")
    @JsonManagedReference
    private Set<Episodes> episodes;

    public Series(String name, String description, Set<SeriesTags> seriesTags) {
        this.name = name;
        this.description = description;
        this.seriesTags = seriesTags;
    }

    public Series(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
