package com.hackathon.KCThack.dto;


import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddPointsRequest {

    @Min(1)
    int points;


}
