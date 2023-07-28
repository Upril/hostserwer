package com.serwertetowy.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//Entity for storing episode info in the db using Spring data jpa
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
    @NotNull
    private Series series;
    @NotEmpty
    @Size(min = 1,max = 255)
    private String title;
    @NotNull
    private List<String> languages;
    //may add episode icon

    public Episodes(String name, Series series, List<String> langs){
        this.languages = langs;
        this.series = series;
        this.title = name;
    }


}
