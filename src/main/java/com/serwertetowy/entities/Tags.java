package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tags {
    @Id
    @SequenceGenerator(name = "tag_id_sequence", sequenceName = "tag_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_sequence")
    private Long id;
    private String name;
    @OneToMany(mappedBy = "tags")
    private Set<SeriesTags> seriesTags;

}
