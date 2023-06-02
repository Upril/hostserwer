package com.serwertetowy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
    private Long id;
    @Column(name = "firstName")
    private String firstname;
    @Column(name = "lastName")
    private String lastname;
    private String email;
    @JsonIgnore
    private String password;
    @Lob
    @Column(name = "imageData", length = 1000)
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;
    @OneToMany(mappedBy = "user")
    private Set<Rating> userRatings;
    @JsonProperty
    public void setPassword(String password){
        this.password = password;
    }
}
