package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.HandStrength;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.scalabootcamp.poker.model.HandStrength.FLUSH;
import static com.scalabootcamp.poker.model.HandStrength.FOUR_OF_A_KIND;
import static com.scalabootcamp.poker.model.HandStrength.FULL_HOUSE;
import static com.scalabootcamp.poker.model.HandStrength.HIGH_CARD;
import static com.scalabootcamp.poker.model.HandStrength.PAIR;
import static com.scalabootcamp.poker.model.HandStrength.STRAIGHT;
import static com.scalabootcamp.poker.model.HandStrength.STRAIGHT_FLUSH;
import static com.scalabootcamp.poker.model.HandStrength.THREE_OF_A_KIND;
import static com.scalabootcamp.poker.model.HandStrength.TWO_PAIR;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public final class HandStrengthEvaluator {

    private HandStrengthEvaluator() {
    }

    public static HandStrength evaluateHandStrength(Hand h, List<Card> cardsOnTable) {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(cardsOnTable);
        allCards.addAll(h.getCards());
        if (isAStraightFlush(allCards)) {
            return STRAIGHT_FLUSH;
        } else if (isAFourOfAKind(allCards)) {
            return FOUR_OF_A_KIND;
        } else if (isAFullHouse(allCards)) {
            return FULL_HOUSE;
        } else if (isAFlush(allCards)) {
            return FLUSH;
        } else if (isAStraight(allCards)) {
            return STRAIGHT;
        } else if (isThreeOfAKind(allCards)) {
            return THREE_OF_A_KIND;
        } else if (isTwoPair(allCards)) {
            return TWO_PAIR;
        } else if (isPair(allCards)) {
            return PAIR;
        } else {
            return HIGH_CARD;
        }
    }

    private static boolean isAStraightFlush(List<Card> allCards) {
        return isAFlush(allCards) && isAStraight(allCards);
    }

    private static boolean isAFlush(List<Card> allCards) {
        List<Character> suits = allCards.stream().map(Card::getSuit).collect(toList());
        return suits.stream().collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get().getValue() >= 5;
    }

    private static boolean isAStraight(List<Card> allCards) {
        List<Integer> values = getSortedValues(allCards).stream().distinct().collect(toList());
        int i = 0;
        for (int j = 0; j < values.size() - 1; j++) {
            if (values.get(j + 1) - values.get(j) == 1) {
                i++;
            } else {
                if (i == 4)
                    return true;
                i = 0;
            }
        }
        return i >= 4;
    }

    private static boolean isAFourOfAKind(List<Card> allCards) {
        List<Integer> values = getSortedValues(allCards);
        return getCountOfMostCommonValue(values).getValue() >= 4;
    }

    private static boolean isAFullHouse(List<Card> allCards) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() == 3) {
            List<Integer> finalValues = values;
            values = values.stream()
                    .filter(v -> !v.equals(getCountOfMostCommonValue(finalValues).getKey()))
                    .collect(toList());
            return getCountOfMostCommonValue(values).getValue() == 2;
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> allCards) {
        List<Integer> values = getSortedValues(allCards);
        return getCountOfMostCommonValue(values).getValue() == 3;
    }

    private static boolean isTwoPair(List<Card> allCards) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() == 2) {
            List<Integer> finalValues = values;
            values = values.stream()
                    .filter(v -> !v.equals(getCountOfMostCommonValue(finalValues).getKey()))
                    .collect(toList());
            return getCountOfMostCommonValue(values).getValue() == 2;
        }
        return false;
    }

    private static boolean isPair(List<Card> allCards) {
        List<Integer> values = getSortedValues(allCards);
        return getCountOfMostCommonValue(values).getValue() == 2;
    }

    private static List<Integer> getSortedValues(List<Card> allCards) {
        return allCards.stream()
                .map(Card::getValue)
                .map(v -> {
                    if (v.equals('T'))
                        return 10;
                    else if (v.equals('J'))
                        return 11;
                    else if (v.equals('Q'))
                        return 12;
                    else if (v.equals('K'))
                        return 13;
                    else if (v.equals('A'))
                        return 14;
                    else return Integer.valueOf(v);
                })
                .sorted()
                .collect(toList());
    }

    private static Map.Entry<Integer, Long> getCountOfMostCommonValue(List<Integer> values) {
        return values.stream().collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get();
    }
}
