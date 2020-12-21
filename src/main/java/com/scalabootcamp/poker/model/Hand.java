package com.scalabootcamp.poker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class Hand {
    private List<Card> cards;
    private HandStrength handStrength;
    private int combinationStrength;
}
