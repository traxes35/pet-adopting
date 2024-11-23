package com.example.pet_adopting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;


@Entity
@Data
@Table(name = "pet")
public class Pet {
    @Id
    Long id;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name ="user_id",nullable = false)
@OnDelete(action= OnDeleteAction.CASCADE)
@JsonIgnore
private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id",nullable = false) // Foreign key
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="type_id",nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private Type type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="breed_id",nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private Breed breed;
    private String name;
    private int age;
    private String gender;
    private boolean isVaccinated;
    private boolean isActive;
    private Date  CreatedDate;
    private String description;
}
