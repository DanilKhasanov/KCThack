package com.hackathon.KDT_HACK.controller;


import com.hackathon.KDT_HACK.dto.*;
import com.hackathon.KDT_HACK.service.AdminService;
import com.hackathon.KDT_HACK.service.UserPointsService;
import com.hackathon.KDT_HACK.entity.User;
import com.hackathon.KDT_HACK.service.UserService;
import com.hackathon.KDT_HACK.entity.Schedule;
import com.hackathon.KDT_HACK.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final UserPointsService userPointsService;





//Skills
    @PostMapping("/add-skills")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSkills(@RequestBody List<SkillAddDto> skills){
        adminService.addSkills(skills);
        return ResponseEntity.ok(new ResponseDTO("Skills added successfully"));
    }

    @DeleteMapping("/delete-skills")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeSkills(@RequestBody List<SkillRemoveDto> skills){
        adminService.deleteSkills(skills);
        return ResponseEntity.ok(new ResponseDTO("Skills deleted successfully"));
    }

//Users
    @GetMapping("/getall-users")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }

    @PostMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest, id));
    }

    @PostMapping("/block-user/{id}")
    public ResponseEntity<?> blockUser(@PathVariable String id){
        return ResponseEntity.ok().body(adminService.blockUserById(id));
    }
    @PostMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        return ResponseEntity.ok().body(adminService.deleteUserById(id));
    }

//Achievements
    @PostMapping("/add-points/{id}")
    public ResponseEntity<?> addPoints(@PathVariable String id, @RequestBody int points){

        return ResponseEntity.ok().body(userPointsService.addPoints(id, points));
    }


//Schedule
    @PostMapping("/schedule/create-event")
    public ResponseEntity<Schedule>  createEvent(@RequestBody @Valid EventCreateDto eventCreateDto){

        return ResponseEntity.status(201)
                .body(scheduleService.createEvent(eventCreateDto));
    }
    @PutMapping("/schedule/update-event/{id}")
    public ResponseEntity<Schedule> updateEvent(
            @PathVariable("id") Long id, @RequestBody @Valid Schedule eventToUpdate
    ){
        var updated = scheduleService.updateEvent(id, eventToUpdate);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/schedule/delete-event/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id){
            scheduleService.deleteEvent(id);
            return ResponseEntity.ok().build();
    }




}
