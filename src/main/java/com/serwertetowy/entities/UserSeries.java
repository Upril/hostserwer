package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserSeries {
    @Id
    @SequenceGenerator(name = "userseries_id_sequence", sequenceName = "userseries_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userseries_id_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;
    @Column(name = "favourite")
    private boolean isFavourite;
    @ManyToOne
    @JoinColumn(name = "watch_flag_id")
    private WatchFlags watchFlags;


}
