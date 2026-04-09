package com.hackathon.KDT_HACK.Schedule;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class ScheduleService {



    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;


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
    public  Schedule createEvent(Schedule eventToCreate){

        var entityToSave = new ScheduleEntity(
                null,
                eventToCreate.name(),
                eventToCreate.briefDescription(),
                eventToCreate.description(),
                eventToCreate.skills(),
                eventToCreate.startDateTime(),
                eventToCreate.endDateTime()
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
    public void deleteTask(Long id) {
        if (!repository.existsById(id)){
            throw new EntityNotFoundException("Not found task by id: "+ id);
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
