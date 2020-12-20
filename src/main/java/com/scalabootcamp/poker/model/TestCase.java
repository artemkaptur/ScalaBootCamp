package com.scalabootcamp.poker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TestCase {
    private final String gameType;
    private final String cardsOnTable;
    private final List<String> hands;
}
