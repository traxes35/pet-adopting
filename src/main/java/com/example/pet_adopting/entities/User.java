package com.example.pet_adopting.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="user")
public class User {
    @Id
private long id;
private String name;
private String surname;
private String email;
private String password;
private boolean isActive;

}
