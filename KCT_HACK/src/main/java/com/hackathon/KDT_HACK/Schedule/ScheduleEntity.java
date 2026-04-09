package com.hackathon.KDT_HACK.Schedule;


import com.hackathon.KDT_HACK.PersonalAccount.Skills.Skills;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "schedule")
@Entity
@Data
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


}
