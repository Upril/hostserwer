package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Languages {
    @Id
    @SequenceGenerator(name = "lang_id_sequence", sequenceName = "lang_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lang_id_sequence")
    private Integer id;
    private String name;
}
