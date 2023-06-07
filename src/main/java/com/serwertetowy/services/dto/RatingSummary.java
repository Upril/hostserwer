package com.serwertetowy.services.dto;
//Interface Reprojection to filter out unnecessary user and series data from the json returned by the requests
public interface RatingSummary {
    Long getId();
    Long getSeriesId();
    Long getUserId();
    short getPlotRating();
    short getMusicRating();
    short getGraphicsRating();
    short getCharactersRating();
    short getGeneralRating();
}
