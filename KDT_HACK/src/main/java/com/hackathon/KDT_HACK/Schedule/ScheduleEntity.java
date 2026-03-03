package com.hackathon.KDT_HACK.Schedule;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "schedule")
@Entity
public class ScheduleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brief_description", nullable = false)
    private String briefDescription;
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public ScheduleEntity() {
    }

    public ScheduleEntity(Long id, String name, String briefDescription, String description, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.briefDescription = briefDescription;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
