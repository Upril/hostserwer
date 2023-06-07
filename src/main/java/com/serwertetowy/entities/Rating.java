package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @SequenceGenerator(name = "rating_id_sequence", sequenceName = "rating_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_id_sequence")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    private short plotRating;
    private short musicRating;
    private short graphicsRating;
    private short charactersRating;
    private short generalRating;
    //constructor allowing automatic id generation
    public Rating(Series series, User user, short plotRating, short musicRating, short graphicsRating, short charactersRating, short generalRating) {
        this.series = series;
        this.user = user;
        this.plotRating = plotRating;
        this.musicRating = musicRating;
        this.graphicsRating = graphicsRating;
        this.charactersRating = charactersRating;
        this.generalRating = generalRating;
    }
}
