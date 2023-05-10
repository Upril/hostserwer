package com.serwertetowy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WatchFlags {
    @Id
    @SequenceGenerator(name="wflag_id_sequence", sequenceName = "wflag_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wflag_id_sequence")
    private Integer id;
    private String name;
}
