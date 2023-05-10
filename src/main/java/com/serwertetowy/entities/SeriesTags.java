package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SeriesTags {
    @Id
    @SequenceGenerator(name = "seriestag_id_sequence", sequenceName = "seriestag_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seriestag_id_sequence")
    private Integer id;
    @ManyToOne
    private Series series;
    @ManyToOne
    private Tags tags;
}
