package com.hackathon.KDT_HACK.repository;

import com.hackathon.KDT_HACK.entity.Schedule;
import com.hackathon.KDT_HACK.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {


}
