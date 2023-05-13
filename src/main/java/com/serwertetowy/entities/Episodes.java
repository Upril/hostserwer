package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Episodes {
    @Id
    @SequenceGenerator(name = "episode_id_sequence", sequenceName = "episode_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "episode_id_sequence")
    private Integer id;

    @ManyToOne
    private Series series;
    private String title;
    @OneToMany
    private Set<Languages> languages;

    @Lob
    private byte[] data;

    public Episodes(String name, Series series, Set<Languages> langs, byte[] data){
        this.languages = langs;
        this.series = series;
        this.title = name;
        this.data = data;
    }


}
