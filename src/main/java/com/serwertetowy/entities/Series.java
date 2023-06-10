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
    //@JsonManagedReference to allow seriestags db info being updated with adding of the series
    @JsonManagedReference
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private Set<SeriesTags> seriesTags;
    @OneToMany(mappedBy = "series")
    private Set<Rating> seriesRatings;
    //@JsonManagedReference to allow seriestags db info being updated with adding of the series
    @OneToMany(mappedBy = "series")
    @JsonManagedReference
    private Set<Episodes> episodes;
    //series icon
    @Lob
    @Column(name = "imageData", length = 1000)
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;
    //different constructors needed for dynamic translation of series tags data from db into summaries
    //this constructor apparently not used, may be used in the future
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
