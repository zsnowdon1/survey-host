package com.voting.survey_host.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ToggleStatusResponse {
    private String newStatus;
}
