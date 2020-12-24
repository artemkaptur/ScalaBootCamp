package com.scalabootcamp.poker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class TestCase {
    private final String gameType;
    private final String cardsOnTable;
    private final List<String> hands;
    private final String errorMessage;
}
