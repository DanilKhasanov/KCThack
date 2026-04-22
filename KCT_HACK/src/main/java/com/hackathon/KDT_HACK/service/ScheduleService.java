package com.hackathon.KDT_HACK.service;


import com.hackathon.KDT_HACK.dto.EventCreateDto;
import com.hackathon.KDT_HACK.entity.Schedule;
import com.hackathon.KDT_HACK.entity.ScheduleEntity;
import com.hackathon.KDT_HACK.entity.Skills;
import com.hackathon.KDT_HACK.repository.ScheduleRepository;
import com.hackathon.KDT_HACK.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class ScheduleService {



    private final ScheduleRepository repository;
    private final SkillRepository skillRepository;

    public ScheduleService(ScheduleRepository repository, SkillRepository skillRepository) {
        this.repository = repository;
        this.skillRepository = skillRepository;


    }

    public List<Schedule> findAllSchedules(){
        List<ScheduleEntity> allEntities = repository.findAll();

        return allEntities.stream()
                .map(this::toDomainSchedule)
                .toList();
    }

    public Schedule getEventById(Long id) {
        ScheduleEntity scheduleEntity =  repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found task by id: "+ id));
        return toDomainSchedule(scheduleEntity);
    }

    /*
    Будет в админке
     */
    public  Schedule createEvent(EventCreateDto eventToCreate){

        // 1. Загружаем навыки по ID (managed сущности)
        List<Skills> managedSkills = skillRepository.findAllById(eventToCreate.getSkillIds());

        // Проверка: все ли переданные ID существуют?
        if (managedSkills.size() != eventToCreate.getSkillIds().size()) {
            throw new IllegalArgumentException("One or more skill IDs are invalid");
        }

        var entityToSave = new ScheduleEntity(
                null,
                eventToCreate.getName(),
                eventToCreate.getBriefDescription(),
                eventToCreate.getDescription(),
                managedSkills,
                eventToCreate.getStartDateTime(),
                eventToCreate.getEndDateTime()
        );
        var savedEntity = repository.save(entityToSave);
        return toDomainSchedule(savedEntity);
    }
    public Schedule updateEvent(Long id, Schedule eventToUpdate) {
        var scheduleEntity  =repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found task by id: "+ id));

        var scheduleToSave = new ScheduleEntity(
                scheduleEntity.getId(),
                eventToUpdate.name(),
                eventToUpdate.briefDescription(),
                eventToUpdate.description(),
                eventToUpdate.skills(),
                eventToUpdate.startDateTime(),
                eventToUpdate.endDateTime()
        );

        var updatedEvent = repository.save(scheduleToSave);
        return toDomainSchedule(updatedEvent);
    }
    @Transactional
    public void deleteEvent(Long id) {
        if (!repository.existsById(id)){
            throw new EntityNotFoundException("Not found event by id: "+ id);
        }
        repository.deleteById(id);
    }



    private Schedule toDomainSchedule(ScheduleEntity schedule){
        return new Schedule(
                schedule.getId(),
                schedule.getName(),
                schedule.getBriefDescription(),
                schedule.getDescription(),
                schedule.getSkills(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()


        );

    }



}
