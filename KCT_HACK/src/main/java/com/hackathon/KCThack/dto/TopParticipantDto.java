package com.hackathon.KCThack.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class TopParticipantDto {

    private Long registrationId;

    @Min(1) @Max(10)
    private Integer place;
}
