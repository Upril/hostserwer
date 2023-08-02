package com.serwertetowy.services.dto;
//Reprojection interface used for selective db data retrieval
public interface UserSummary {
    Long getId();
    String getFirstname();
    String getLastname();
    String getEmail();
    Boolean getDeleted();
}
