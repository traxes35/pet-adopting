package com.example.pet_adopting.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "type")

public class Type {
@Id
private long id;
private String name;

}
