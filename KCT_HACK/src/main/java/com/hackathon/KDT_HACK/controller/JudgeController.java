package com.hackathon.KDT_HACK.controller;

import com.hackathon.KDT_HACK.dto.ResultsHackatonRequest;
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
