package com.hackathon.KCThack.TeamManagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Лимиты команд и правила регистрации на хакатоны (настраиваются в application.properties).
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.team")
public class TeamProperties {

    /**
     * Минимальное число участников для регистрации команды на хакатон.
     */
    private int minMembersForHackathonRegistration = 2;

    /**
     * Максимальное число участников в одной команде.
     */
    private int maxMembers = 5;

    /**
     * Запретить регистрацию на второй хакатон, если интервалы дат пересекаются (идентификатор — id события в расписании).
     */
    private boolean forbidOverlappingHackathonRegistrations = true;
}
