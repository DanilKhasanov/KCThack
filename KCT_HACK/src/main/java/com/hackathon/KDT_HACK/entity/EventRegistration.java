package com.hackathon.KDT_HACK.entity;

import com.hackathon.KDT_HACK.enums.RegistrationStatus;
import com.hackathon.KDT_HACK.enums.RegistrationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "schedule_id"}) // один пользователь на событие – одна запись
        })
@Getter @Setter
@NoArgsConstructor
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    // Тип регистрации: SOLO или TEAM (если поддерживаете команды)
    @Enumerated(EnumType.STRING)
    @Column(name = "registration_type", nullable = false)
    private RegistrationType type;

//    // Если регистрация в составе команды – ссылка на команду
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "team_id")
//    private Team team;

    // Статус регистрации (ACTIVE, CANCELLED и т.д.)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.ACTIVE;

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
}
