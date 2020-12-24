package com.scalabootcamp.poker.businesslogic;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public abstract class PokerGame {

    public static final int CARDS_ON_TABLE_ALL_SYMBOLS = 10;
    protected static final Comparator<Hand> handComparator = comparingInt(Hand::getHandStrength)
            .thenComparingInt(Hand::getCombinationStrength)
            .thenComparingInt(Hand::getSecondKicker)
            .thenComparingInt(Hand::getThirdKicker)
            .thenComparingInt(Hand::getFourthKicker)
            .thenComparingInt(Hand::getFifthKicker);
    protected static final Comparator<Hand> handComparatorWithAlphabeticalOrder = handComparator
            .thenComparing((h1, h2) -> {
                String h1String = h1.getCards().stream()
                        .map(c -> String.valueOf(c.getValue()) + c.getSuit())
                        .collect(joining());
                String h2String = h2.getCards().stream()
                        .map(c -> String.valueOf(c.getValue()) + c.getSuit())
                        .collect(joining());
                return h1String.compareTo(h2String);
            });

    public abstract String sortHands(TestCase testCase);

    protected static List<Hand> getHandsFromTestCase(TestCase testCase) {
        return testCase.getHands().stream()
                .map(h -> Hand.builder()
                        .cards(getHandCards(h)).build())
                .collect(toList());
    }

    protected static List<Card> getHandCards(String hand) {
        List<Card> handCards = new ArrayList<>();
        for (int counter = 0; counter < hand.length() - 1; counter += 2) {
            handCards.add(new Card(hand.charAt(counter), hand.charAt(counter + 1)));
        }
        return handCards;
    }

    protected static String sortedHandsToString(List<Hand> hands) {
        List<String> sortedHands = new ArrayList<>();
        for (int counter = 0; counter < hands.size(); counter++) {
            StringBuilder sb = new StringBuilder();
            for (Card card : hands.get(counter).getCards()) {
                sb.append(card.getValue());
                sb.append(card.getSuit());
            }
            if (counter != hands.size() - 1) {
                if (handComparator.compare(hands.get(counter), hands.get(counter + 1)) == 0) {
                    sb.append("=");
                }
            }
            sortedHands.add(sb.toString());
        }
        return String.join(" ", sortedHands).replaceAll("= ", "=");
    }
}
