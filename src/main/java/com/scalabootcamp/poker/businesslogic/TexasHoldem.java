package com.scalabootcamp.poker.businesslogic;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.TestCase;

import java.util.ArrayList;
import java.util.List;

import static com.scalabootcamp.poker.HandStrengthEvaluator.evaluateHandStrength;

public class TexasHoldem extends PokerGame {

    public String sortHands(TestCase testCase) {
        List<Card> cardsOnTable = new ArrayList<>();
        for (int counter = 0; counter < CARDS_ON_TABLE_ALL_SYMBOLS; counter += 2) {
            Card cardOnTable = new Card(testCase.getCardsOnTable().charAt(counter), testCase.getCardsOnTable().charAt(counter + 1));
            cardsOnTable.add(cardOnTable);
        }
        List<Hand> hands = getHandsFromTestCase(testCase);
        hands.forEach(h -> {
            List<Card> allCards = new ArrayList<>(cardsOnTable);
            allCards.addAll(h.getCards());
            evaluateHandStrength(h, allCards);
        });
        hands.sort(handComparatorWithAlphabeticalOrder);
        return sortedHandsToString(hands);
    }
}
