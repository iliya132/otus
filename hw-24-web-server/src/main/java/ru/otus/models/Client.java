package ru.otus.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    public Client(long id, String name, LocalDateTime createdAt){
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Client(long id, String name){
        this.id = id;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public Client(String name) {
        this.id = 0;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public Client() {
        this.id = 0;
        this.name = "";
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
