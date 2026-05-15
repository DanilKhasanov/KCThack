package com.hackathon.KCThack.entity;

import com.hackathon.KCThack.TeamManagement.model.Team;
import com.hackathon.KCThack.enums.RegistrationStatus;
import com.hackathon.KCThack.enums.RegistrationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "schedule_id"}),
                @UniqueConstraint(columnNames = {"team_id", "schedule_id"})
        })
@Getter @Setter
@NoArgsConstructor
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "registration_type", nullable = false)
    private RegistrationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.ACTIVE;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "result")
    private String result;

    @Column(name = "place")
    private int place;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRegistration evReg)) return false;
        return id != null && id.equals(evReg.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PrePersist
    public void prePersist() {
        if (registeredAt == null) {
            registeredAt = Instant.now();
        }
    }
}
