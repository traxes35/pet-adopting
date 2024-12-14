package com.example.pet_adopting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Builder
@Data
@Entity
@Table(name = "breed")
@AllArgsConstructor
public class Breed {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
private long id;
private String name;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name ="type_id",nullable = false)
@OnDelete(action= OnDeleteAction.CASCADE)
@JsonIgnore
Type type;

    public Breed() {
    }
}
