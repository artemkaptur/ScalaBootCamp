package com.scalabootcamp.poker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Card {
    private final Character value;
    private final Character suit;
}
