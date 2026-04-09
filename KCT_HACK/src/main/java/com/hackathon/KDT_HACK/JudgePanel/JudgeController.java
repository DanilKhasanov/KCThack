package com.hackathon.KDT_HACK.JudgePanel;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/judge")
public class JudgeController {

    @GetMapping("/hackathon-results")
    @PreAuthorize("hasRole('JUDGE')")
    public String judge(@RequestBody ResultsHackatonRequest results){

        return "";
    }
}
