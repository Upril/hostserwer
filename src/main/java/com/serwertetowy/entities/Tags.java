package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tags {
    @Id
    @SequenceGenerator(name = "tag_id_sequence", sequenceName = "tag_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_sequence")
    private Integer id;
    private String name;

}
