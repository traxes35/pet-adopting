package com.example.pet_adopting.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "city")
public class City {
    @Id
    private long id;
    private String name;
    private int plate;
}
