package com.serwertetowy.services.dto;

import java.util.List;
//Interface Reprojection to filter out the video data from requests that don't need to have it, may include icons in the future
public interface EpisodeSummary {
    String getTitle();
    Long getId();
    List<String> getLanguages();

}
