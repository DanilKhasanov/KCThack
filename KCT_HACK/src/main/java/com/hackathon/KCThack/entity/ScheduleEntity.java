package com.hackathon.KCThack.entity;

import com.hackathon.KCThack.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Table(name = "schedule")
@Entity
@Getter @Setter
@AllArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brief_description", nullable = false)
    private String briefDescription;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "schedule_skills",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skills> skills;

    // Моменты старта и окончания в UTC
    @Column(name = "start_date", nullable = false)
    private Instant startDateTime;

    @Column(name = "end_date", nullable = false)
    private Instant endDateTime;

    @Column(name = "result_published", nullable = false)
    private boolean resultsPublished = false;

    public ScheduleEntity() {
    }

    /**
     * Вычисляемый статус: ожидается / идёт / завершено.
     * Не хранится в БД, определяется по текущему времени UTC.
     */
    @Transient
    public ScheduleStatus getStatus() {
        Instant now = Instant.now();
        if (now.isBefore(startDateTime)) {
            return ScheduleStatus.PENDING;
        } else if (now.isAfter(endDateTime)) {
            return ScheduleStatus.COMPLETED;
        } else {
            return ScheduleStatus.IN_PROGRESS;
        }
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