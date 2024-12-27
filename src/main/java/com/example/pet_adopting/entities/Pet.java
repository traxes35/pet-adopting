package com.example.pet_adopting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@Table(name = "pet")
public class Pet {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
@ManyToOne(fetch = FetchType.EAGER)
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
  //  @CreatedDate // Tarihi otomatik oluşturur
   // @Column(nullable = false, updatable = false) // Bu alan güncellenemez
   // private LocalDateTime createdDate;
    private String description;
    private String imagePath;

}
