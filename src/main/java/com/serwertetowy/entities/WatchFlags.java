package com.serwertetowy.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private Long id;
    @NotBlank
    @Size(min = 1)
    private String name;
}
