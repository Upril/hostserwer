package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
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
    @OneToMany(mappedBy = "series")
    private Set<SeriesTags> seriesTags;
    @OneToMany(mappedBy = "series")
    private Set<Rating> seriesRatings;
    @OneToMany(mappedBy = "series")
    private Set<Episodes> episodes;


}
