package com.serwertetowy.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
//entity for linking the tags to the series
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SeriesTags {
    @Id
    @SequenceGenerator(name = "seriestag_id_sequence", sequenceName = "seriestag_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seriestag_id_sequence")
    private Long id;
    @JsonBackReference
    @ManyToOne
    private Series series;
    @ManyToOne
    private Tags tags;
    //constructor for series tags initialisation without the id
    public SeriesTags(Series series, Tags tags) {
        this.series = series;
        this.tags = tags;
    }
}
