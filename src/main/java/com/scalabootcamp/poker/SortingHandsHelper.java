package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.scalabootcamp.poker.HandStrengthEvaluator.evaluateHandStrength;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public final class SortingHandsHelper {

    private static final Comparator<Hand> handComparator = comparingInt(Hand::getHandStrength)
            .thenComparingInt(Hand::getCombinationStrength)
            .thenComparingInt(Hand::getSecondKicker)
            .thenComparingInt(Hand::getThirdKicker)
            .thenComparingInt(Hand::getFourthKicker)
            .thenComparingInt(Hand::getFifthKicker);

    private SortingHandsHelper() {
    }

    public static String sortHandsForTexasHoldem(TestCase testCase) {
        List<Card> cardsOnTable = new ArrayList<>();
        for (int i = 0; i < 10; i += 2) {
            cardsOnTable.add(new Card(testCase.getCardsOnTable().charAt(i), testCase.getCardsOnTable().charAt(i + 1)));
        }
        List<Hand> hands = getHandsFromTestCase(testCase);
        hands.forEach(h -> {
            List<Card> allCards = new ArrayList<>();
            allCards.addAll(cardsOnTable);
            allCards.addAll(h.getCards());
            evaluateHandStrength(h, allCards);
        });
        hands.sort(handComparator);
        return sortedHandsToString(hands);
    }


    public static String sortHandsForFiveCardDraw(TestCase testCase) {
        List<Hand> hands = getHandsFromTestCase(testCase);
        hands.forEach(h -> evaluateHandStrength(h, h.getCards()));
        hands.sort(handComparator);
        return sortedHandsToString(hands);
    }

    private static List<Hand> getHandsFromTestCase(TestCase testCase) {
        return testCase.getHands().stream()
                .map(h -> Hand.builder()
                        .cards(getHandCards(h)).build())
                .collect(toList());
    }

    private static List<Card> getHandCards(String hand) {
        List<Card> handCards = new ArrayList<>();
        for (int i = 0; i < hand.length() - 1; i += 2) {
            handCards.add(new Card(hand.charAt(i), hand.charAt(i + 1)));
        }
        return handCards;
    }

    private static String sortedHandsToString(List<Hand> hands) {
        List<String> sortedHands = new ArrayList<>();
        for (int i = 0; i < hands.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (Card card : hands.get(i).getCards()) {
                sb.append(card.getValue());
                sb.append(card.getSuit());
            }
            if (i != hands.size() - 1) {
                if (handComparator.compare(hands.get(i), hands.get(i + 1)) == 0) {
                    sb.append("=");
                }
            }
            sortedHands.add(sb.toString());
        }
        return String.join(" ", sortedHands).replaceAll("= ", "=");
    }
}
