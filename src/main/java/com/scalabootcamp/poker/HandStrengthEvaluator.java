package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;
import com.scalabootcamp.poker.model.HandStrength;

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

    public static HandStrength evaluateHandStrength(List<Card> allCards, Hand hand) {
        if (isAStraightFlush(allCards, hand)) {
            return STRAIGHT_FLUSH;
        } else if (isAFourOfAKind(allCards, hand)) {
            return FOUR_OF_A_KIND;
        } else if (isAFullHouse(allCards, hand)) {
            return FULL_HOUSE;
        } else if (isAFlush(allCards, hand)) {
            return FLUSH;
        } else if (isAStraight(allCards, hand)) {
            return STRAIGHT;
        } else if (isThreeOfAKind(allCards, hand)) {
            return THREE_OF_A_KIND;
        } else if (isTwoPair(allCards, hand)) {
            return TWO_PAIR;
        } else if (isPair(allCards, hand)) {
            return PAIR;
        } else {
            hand.setCombinationStrength(getSortedValues(hand.getCards()).stream()
                    .distinct()
                    .max(Integer::compare).get());
            return HIGH_CARD;
        }
    }

    private static boolean isAStraightFlush(List<Card> allCards, Hand hand) {
        return isAFlush(allCards, hand) && isAStraight(allCards, hand);
    }

    private static boolean isAFlush(List<Card> allCards, Hand hand) {
        List<Character> suits = allCards.stream().map(Card::getSuit).collect(toList());
        Long countOfMostCommonSuit = suits.stream().collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get().getValue();

        if (countOfMostCommonSuit >= 5) {
            Character flushSuit = suits.stream().collect(groupingBy(Function.identity(), counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue()).get().getKey();
            List<Card> handCardsWithFlush = hand.getCards().stream().filter(c -> c.getSuit().equals(flushSuit)).collect(toList());
            List<Integer> values = getSortedValues(handCardsWithFlush).stream().distinct().collect(toList());
            hand.setCombinationStrength(values.stream().max(Integer::compare).get());
            return true;
        }
        return false;
    }

    private static boolean isAStraight(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards).stream().distinct().collect(toList());
        int i = 0;
        for (int j = 0; j < values.size() - 1; j++) {
            if (values.get(j + 1) - values.get(j) == 1) {
                i++;
                hand.setCombinationStrength(values.get(j + 1));
            } else {
                if (i == 4) {
                    hand.setCombinationStrength(values.get(j));
                    return true;
                }
                i = 0;
            }
        }
        return i >= 4;
    }

    private static boolean isAFourOfAKind(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() >= 4) {
            hand.setCombinationStrength(getCountOfMostCommonValue(values).getKey());
            return true;
        }
        return false;
    }

    private static boolean isAFullHouse(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() == 3) {
            int combinationStrength = getCountOfMostCommonValue(values).getKey();
            List<Integer> finalValues = values;
            values = values.stream()
                    .filter(v -> !v.equals(getCountOfMostCommonValue(finalValues).getKey()))
                    .collect(toList());
            if (getCountOfMostCommonValue(values).getValue() >= 2) {
                hand.setCombinationStrength(combinationStrength);
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() >= 3) {
            hand.setCombinationStrength(getCountOfMostCommonValue(values).getKey());
            return true;
        }
        return false;
    }

    private static boolean isTwoPair(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() == 2) {
            hand.setCombinationStrength(getCountOfMostCommonValue(values).getKey());
            List<Integer> finalValues = values;
            values = values.stream()
                    .filter(v -> !v.equals(getCountOfMostCommonValue(finalValues).getKey()))
                    .collect(toList());
            if (getCountOfMostCommonValue(values).getValue() == 2) {
                if (hand.getCombinationStrength() < getCountOfMostCommonValue(values).getKey()) {
                    hand.setCombinationStrength(getCountOfMostCommonValue(values).getKey());
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isPair(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getCountOfMostCommonValue(values).getValue() >= 2) {
            hand.setCombinationStrength(getCountOfMostCommonValue(values).getKey());
            return true;
        }
        return false;
    }

    private static List<Integer> getSortedValues(List<Card> allCards) {
        List<Integer> values = allCards.stream().map(c -> Character.getNumericValue(c.getValue())).collect(toList());
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
                    else if (v.equals('A') &&
                            values.contains(2) &&
                            values.contains(3) &&
                            values.contains(4) &&
                            values.contains(5))
                        return 1;
                    else if (v.equals('A'))
                        return 14;
                    else return Character.getNumericValue(v);
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
