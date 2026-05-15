package com.hackathon.KCThack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class TopParticipantsRequest {

    private List<TopParticipantDto> topParticipants;
}
