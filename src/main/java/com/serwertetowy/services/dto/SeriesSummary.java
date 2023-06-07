package com.serwertetowy.services.dto;
import com.serwertetowy.entities.Tags;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//dto used for returning series data without episodes videos, but with detailed tag information
@Data
@NoArgsConstructor
public class SeriesSummary {
    Long id;
    String name;
    String description;
    List<Tags> seriesTags;
    List<EpisodeSummary> episodes;

}
