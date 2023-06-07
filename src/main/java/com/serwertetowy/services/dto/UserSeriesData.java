package com.serwertetowy.services.dto;
//reprojection interface for selecting specific information about the watchlists from the db
public interface UserSeriesData {
    Long getId();
    Long getSeriesId();
    Long getUserId();
    Long getWatchFlagsId();
}
