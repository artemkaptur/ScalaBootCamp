package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.TestCase;

import java.util.ArrayList;
import java.util.List;

import static com.scalabootcamp.poker.HandStrengthEvaluator.evaluateHandStrength;
import static java.util.stream.Collectors.toList;

public final class SortingHandsHelper {

    private SortingHandsHelper() {
    }

    public static String sortHandsForTexasHoldem(TestCase testCase) {
        List<Card> cardsOnTable = new ArrayList<>();
        for (int i = 0; i < 10; i += 2) {
            cardsOnTable.add(new Card(testCase.getCardsOnTable().charAt(i), testCase.getCardsOnTable().charAt(i + 1)));
        }
        List<Hand> hands = testCase.getHands().stream()
                .map(h -> Hand.builder()
                        .cards(new ArrayList<>() {{
                            add(new Card(h.charAt(0), h.charAt(1)));
                            add(new Card(h.charAt(2), h.charAt(3)));
                        }}).build())
                .collect(toList());
        hands.forEach(h -> {
            List<Card> allCards = new ArrayList<>();
            allCards.addAll(cardsOnTable);
            allCards.addAll(h.getCards());
            h.setHandStrength(evaluateHandStrength(allCards));
        });
        hands.sort((Hand h1, Hand h2) -> {
            if (h1.getHandStrength().getValue() > h2.getHandStrength().getValue()) {
                return 1;
            } else {
                return -1;
            }
        });
        List<String> sortedHands = new ArrayList<>();
        for (Hand hand : hands) {
            StringBuilder sb = new StringBuilder();
            sb.append(hand.getCards().get(0).getValue());
            sb.append(hand.getCards().get(0).getSuit());
            sb.append(hand.getCards().get(1).getValue());
            sb.append(hand.getCards().get(1).getSuit());
            sortedHands.add(sb.toString());
        }
        return String.join(" ", sortedHands);
    }

    public static String sortHandsForFiveCardDraw(TestCase testCase) {
        List<Hand> hands = testCase.getHands().stream()
                .map(h -> Hand.builder()
                        .cards(new ArrayList<>() {{
                            add(new Card(h.charAt(0), h.charAt(1)));
                            add(new Card(h.charAt(2), h.charAt(3)));
                            add(new Card(h.charAt(4), h.charAt(5)));
                            add(new Card(h.charAt(6), h.charAt(7)));
                            add(new Card(h.charAt(8), h.charAt(9)));
                        }}).build())
                .collect(toList());
        hands.forEach(h -> h.setHandStrength(evaluateHandStrength(h.getCards())));
        hands.sort((Hand h1, Hand h2) -> {
            if (h1.getHandStrength().getValue() > h2.getHandStrength().getValue()) {
                return 1;
            } else {
                return -1;
            }
        });
        List<String> sortedHands = new ArrayList<>();
        for (Hand hand : hands) {
            StringBuilder sb = new StringBuilder();
            sb.append(hand.getCards().get(0).getValue());
            sb.append(hand.getCards().get(0).getSuit());
            sb.append(hand.getCards().get(1).getValue());
            sb.append(hand.getCards().get(1).getSuit());
            sb.append(hand.getCards().get(2).getValue());
            sb.append(hand.getCards().get(2).getSuit());
            sb.append(hand.getCards().get(3).getValue());
            sb.append(hand.getCards().get(3).getSuit());
            sb.append(hand.getCards().get(4).getValue());
            sb.append(hand.getCards().get(4).getSuit());
            sortedHands.add(sb.toString());
        }
        return String.join(" ", sortedHands);
    }
}
