package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
    private Long id;
    //user one user many ratings
    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;
    private short plotRating;
    private short musicRating;
    private short graphicsRating;
    private short charactersRating;
    private short generalRating;
}
