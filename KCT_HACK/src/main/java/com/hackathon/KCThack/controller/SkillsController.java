package com.hackathon.KCThack.controller;

import com.hackathon.KCThack.entity.Skills;
import com.hackathon.KCThack.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/skills")
public class SkillsController {

    private final SkillRepository skillRepository;

    @GetMapping("/getall-skills")
    public List<Skills> getAllSkills(){

        return skillRepository.findAll();

    }

}
