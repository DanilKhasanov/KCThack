package com.hackathon.KCThack.controller;



import com.hackathon.KCThack.dto.ScheduleDto;
import com.hackathon.KCThack.service.ScheduleService;
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
    public ResponseEntity<List<ScheduleDto>> getAllSchedules(){
        log.info("Called getAllSchedules");
        return ResponseEntity.ok(scheduleService.findAllSchedules());

    }
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> getEventById(@PathVariable("id") Long id) {
        log.info("Called getTaskById: id{}", id);
        
        return ResponseEntity.ok().body(scheduleService.getEventById(id));

    }







}
