package com.example.pet_adopting.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
@Entity
@Table(name = "city")
@AllArgsConstructor
public class City {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String name;
    private int plate;

    public City() {
    }
}
