package com.serwertetowy.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Episodes{
    @Id
    @SequenceGenerator(name = "episode_id_sequence", sequenceName = "episode_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "episode_id_sequence")
    private Long id;

    @ManyToOne
    @JsonManagedReference
    private Series series;
    private String title;
    //private Set<String> languages;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    public Episodes(String name, Series series/*, Set<String> langs*/, byte[] data){
        //this.languages = langs;
        this.series = series;
        this.title = name;
        this.data = data;
    }


}
