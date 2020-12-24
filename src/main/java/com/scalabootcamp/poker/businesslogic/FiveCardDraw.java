package com.scalabootcamp.poker.businesslogic;

import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.TestCase;

import java.util.List;

import static com.scalabootcamp.poker.model.HandStrengthEvaluator.evaluateHandStrength;

public class FiveCardDraw extends PokerGame {

    public String sortHands(TestCase testCase) {
        List<Hand> hands = getHandsFromTestCase(testCase);
        hands.forEach(h -> evaluateHandStrength(h, h.getCards()));
        hands.sort(handComparatorWithAlphabeticalOrder);
        return sortedHandsToString(hands);
    }
}
