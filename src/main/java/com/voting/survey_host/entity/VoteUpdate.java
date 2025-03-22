package com.voting.survey_host.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class VoteUpdate {
    private String questionId;
    private String choiceId;
    private long votes;
}
