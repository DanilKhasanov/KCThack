package com.hackathon.KCThack.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "schedule")
@Entity
@Getter @Setter
@AllArgsConstructor
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

    // Связь многие-ко-многим: у расписания много навыков, навык может быть во многих расписаниях
    @ManyToMany
    @JoinTable(
            name = "schedule_skills",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skills> skills;


    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDateTime;




    public ScheduleEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleEntity schEnt)) return false;
        return id != null && id.equals(schEnt.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
