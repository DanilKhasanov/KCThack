package com.hackathon.KCThack.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "achievements")
public class Achievements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "icon")
    private String icon;

    @Column(name = "points_required")
    private int pointsRequired;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Achievements that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Achievements{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pointsRequired=" + pointsRequired +
                '}';
    }
}