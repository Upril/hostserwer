package com.serwertetowy.services;
import com.serwertetowy.entities.Tags;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class SeriesSummary {
    Long id;
    String name;
    String description;
    List<Tags> seriesTags;
    List<EpisodeSummary> episodes;

}
