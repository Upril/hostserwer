package com.serwertetowy.services;

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
