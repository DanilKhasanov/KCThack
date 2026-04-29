package com.hackathon.KCThack.controller;

import com.hackathon.KCThack.dto.ResultsHackatonRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/judge")
public class JudgeController {

    @PostMapping("/hackathon-results")
    @PreAuthorize("hasRole('JUDGE')")
    public String judge(@RequestBody ResultsHackatonRequest results){

        return "";
    }
}
