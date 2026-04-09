package com.hackathon.KDT_HACK.AdminPanel;


import com.hackathon.KDT_HACK.JudgePanel.UserPointsService;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.Skills;
import com.hackathon.KDT_HACK.PersonalAccount.UpdateUserRequest;
import com.hackathon.KDT_HACK.Registration.User;
import com.hackathon.KDT_HACK.Registration.UserService;
import com.hackathon.KDT_HACK.Response.ResponseDTO;
import com.hackathon.KDT_HACK.Schedule.Schedule;
import com.hackathon.KDT_HACK.Schedule.ScheduleService;
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
    public ResponseEntity<?> SkillsToAdd(@RequestBody List<Skills> skills){
        adminService.addSkills(skills);
        return ResponseEntity.ok(new ResponseDTO("Skills added successfully"));
    }

    @DeleteMapping("/delete-skills")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> SkillsToRemove(@RequestBody List<Skills> skills){
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
            @PathVariable("id") String id,
            UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok().body(userService.updateUser(updateUserRequest, id));
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
    public ResponseEntity<Schedule>  createEvent(@RequestBody @Valid Schedule eventToCreate){

        return ResponseEntity.status(201)
                .body(scheduleService.createEvent(eventToCreate));
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
            scheduleService.deleteTask(id);
            return ResponseEntity.ok().build();
    }




}
