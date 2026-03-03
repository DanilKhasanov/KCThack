package com.hackathon.KDT_HACK.Registration;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AdminController {

    final private SkillRepository skillRepository;



    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(){
        return "админка";
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpStatus httpStatus(@RequestBody Skills skills){
        skillRepository.save(skills);
        return HttpStatus.OK;
    }


    @GetMapping("/judge")
    @PreAuthorize("hasRole('JUDGE')")
    public String judge(){
        return "судья";
    }


}
