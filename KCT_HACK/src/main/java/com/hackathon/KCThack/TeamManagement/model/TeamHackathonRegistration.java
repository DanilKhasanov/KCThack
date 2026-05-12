package com.hackathon.KCThack.TeamManagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "team_hackathon_registrations",
        uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = {"team_id", "event_id"})
)
public class TeamHackathonRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    @ToString.Exclude
    private Team team;

    @Column(name = "event_id", nullable = false)

    private Long eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "registered_by", nullable = false)
    private String registeredBy;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @PrePersist
    public void prePersist() {
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
    }
}
