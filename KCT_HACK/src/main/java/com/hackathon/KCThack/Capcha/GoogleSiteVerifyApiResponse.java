package com.hackathon.KCThack.Capcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Ответ Google siteverify (v2 и v3).
 */
@Data
class GoogleSiteVerifyApiResponse {

    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    private String hostname;

    @JsonProperty("error-codes")
    private List<String> errorCodes;

    private Double score;

    private String action;
}
