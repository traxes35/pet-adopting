package com.example.pet_adopting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "Messages")
@Data
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(columnDefinition = "text")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false) // Sender için farklı bir sütun adı belirleniyor
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User sender; // Değişken adı küçük harfle başlatıldı

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false) // Receiver için farklı bir sütun adı belirleniyor
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User receiver; // Değişken adı küçük harfle başlatıldı

    @Temporal(TemporalType.TIMESTAMP) // Tarih alanı için doğru anotasyon
    @Column(nullable = false, updatable = false)
    private Date createdDate;
}
