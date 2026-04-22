package com.hackathon.KDT_HACK.controller;



import com.hackathon.KDT_HACK.entity.Schedule;
import com.hackathon.KDT_HACK.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/schedule")
public class ScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService){

        this.scheduleService = scheduleService;
    }
    @GetMapping({"" , "/admin"})
    public ResponseEntity<List<Schedule>> getAllSchedules(){
        log.info("Called getAllSchedules");
        return ResponseEntity.ok(scheduleService.findAllSchedules());

    }
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getEventById(@PathVariable("id") Long id) {
        log.info("Called getTaskById: id{}", id);
        
        return ResponseEntity.ok().body(scheduleService.getEventById(id));

    }


    /*
      Будет в админке
     */

//    @PostMapping("/admin")
//    public ResponseEntity<Schedule>  createEvent(@RequestBody @Valid Schedule eventToCreate){
//        log.info("Called createTask");
//        return ResponseEntity.status(201)
//                .body(scheduleService.createEvent(eventToCreate));
//    }
//    @PutMapping("/admin/{id}")
//    public ResponseEntity<Schedule> updateEvent(
//            @PathVariable("id") Long id, @RequestBody @Valid Schedule eventToUpdate
//    ){
//        log.info("Called updateEvent id={}, eventToUpdate={}", id, eventToUpdate);
//        var updated = scheduleService.updateEvent(id, eventToUpdate);
//        return ResponseEntity.ok(updated);
//    }
//    @DeleteMapping("/admin/{id}")
//    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id){
//        log.info("Called deleteEvent: id={}", id);
//            scheduleService.deleteEvent(id);
//            return ResponseEntity.ok().build();
//    }






}
