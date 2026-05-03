package com.hackathon.KCThack.controller;


import com.hackathon.KCThack.dto.UserRatingDto;
import com.hackathon.KCThack.dto.UserRatingRawDto;
import com.hackathon.KCThack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard/")
@RequiredArgsConstructor
public class LeaderboardController {

    private final UserService userService;

    @GetMapping("")
    public List<UserRatingDto> getLeaderboard(){
        return userService.getLeaderboard();
    }



}
