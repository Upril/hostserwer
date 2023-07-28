package com.serwertetowy.entities;

import com.serwertetowy.services.dto.RatingSummary;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;
    @ManyToOne
    @NotNull
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @NotNull(message = "All ratings are mandatory")
    @Min(value = 1,message = "Values of ratings must be between 1 and 10")
    @Max(value = 10,message = "Values of ratings must be between 1 and 10")
    private short plotRating;
    @NotNull(message = "All ratings are mandatory")
    @Min(value = 1,message = "Values of ratings must be between 1 and 10")
    @Max(value = 10,message = "Values of ratings must be between 1 and 10")
    private short musicRating;
    @NotNull(message = "All ratings are mandatory")
    @Min(value = 1,message = "Values of ratings must be between 1 and 10")
    @Max(value = 10,message = "Values of ratings must be between 1 and 10")
    private short graphicsRating;
    @NotNull(message = "All ratings are mandatory")
    @Min(value = 1,message = "Values of ratings must be between 1 and 10")
    @Max(value = 10,message = "Values of ratings must be between 1 and 10")
    private short charactersRating;
    @NotNull(message = "All ratings are mandatory")
    @Min(value = 1,message = "Values of ratings must be between 1 and 10")
    @Max(value = 10,message = "Values of ratings must be between 1 and 10")
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
    public RatingSummary toRatingSummary(){
        return new RatingSummary() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public Long getSeriesId() {
                return series.getId();
            }

            @Override
            public Long getUserId() {
                return user.getId();
            }

            @Override
            public short getPlotRating() {
                return plotRating;
            }

            @Override
            public short getMusicRating() {
                return musicRating;
            }

            @Override
            public short getGraphicsRating() {
                return graphicsRating;
            }

            @Override
            public short getCharactersRating() {
                return charactersRating;
            }

            @Override
            public short getGeneralRating() {
                return generalRating;
            }
        };
    }
}
