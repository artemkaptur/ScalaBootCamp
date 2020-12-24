package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.Hand;

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

    private static final int COUNT_OF_CARDS_FOR_FLUSH = 5;
    private static final int COUNT_OF_CARDS_FOR_FOUR_OF_A_KIND = 4;
    private static final int COUNT_OF_CARDS_FOR_THREE_OF_A_KIND = 3;
    private static final int COUNT_OF_CARDS_FOR_PAIR = 2;

    private HandStrengthEvaluator() {
    }

    public static void evaluateHandStrength(Hand hand, List<Card> allCards) {
        if (isAStraightFlush(allCards, hand)) {
            hand.setHandStrength(STRAIGHT_FLUSH.getValue());
        } else if (isAFourOfAKind(allCards, hand)) {
            hand.setHandStrength(FOUR_OF_A_KIND.getValue());
        } else if (isAFullHouse(allCards, hand)) {
            hand.setHandStrength(FULL_HOUSE.getValue());
        } else if (isAFlush(allCards, hand)) {
            hand.setHandStrength(FLUSH.getValue());
        } else if (isAStraight(allCards, hand)) {
            hand.setHandStrength(STRAIGHT.getValue());
        } else if (isThreeOfAKind(allCards, hand)) {
            hand.setHandStrength(THREE_OF_A_KIND.getValue());
        } else if (isTwoPair(allCards, hand)) {
            hand.setHandStrength(TWO_PAIR.getValue());
        } else if (isPair(allCards, hand)) {
            hand.setHandStrength(PAIR.getValue());
        } else {
            hand.setHandStrength(HIGH_CARD.getValue());
            setKickersForTheHighCardCombination(hand, hand.getCards());
        }
    }

    private static boolean isAStraightFlush(List<Card> allCards, Hand hand) {
        return isAFlush(allCards, hand) && isAStraight(allCards, hand);
    }

    private static boolean isAFlush(List<Card> allCards, Hand hand) {
        List<Character> suits = allCards.stream().map(Card::getSuit).collect(toList());
        Long countOfMostCommonSuit = getMostCommonSuit(suits).getValue();

        if (countOfMostCommonSuit >= COUNT_OF_CARDS_FOR_FLUSH) {
            Character flushSuit = getMostCommonSuit(suits).getKey();
            List<Card> handCardsWithFlush = hand.getCards().stream().filter(c -> c.getSuit().equals(flushSuit)).collect(toList());
            setKickersForTheHighCardCombination(hand, handCardsWithFlush);
            return true;
        }
        return false;
    }

    private static boolean isAStraight(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards).stream().distinct().collect(toList());
        int countOfCardsForStraight = 0;
        for (int counter = 0; counter < values.size() - 1; counter++) {
            if (values.get(counter + 1) - values.get(counter) == 1) {
                countOfCardsForStraight++;
                hand.setCombinationStrength(values.get(counter + 1));
            } else {
                if (countOfCardsForStraight == 4) {
                    hand.setCombinationStrength(values.get(counter));
                    return true;
                }
                countOfCardsForStraight = 0;
            }
        }
        return countOfCardsForStraight >= 4;
    }

    private static boolean isAFourOfAKind(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getMostCommonValue(values).getValue() >= COUNT_OF_CARDS_FOR_FOUR_OF_A_KIND) {
            hand.setCombinationStrength(getMostCommonValue(values).getKey());
            setKickers(hand);
            return true;
        }
        return false;
    }

    private static boolean isAFullHouse(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getMostCommonValue(values).getValue() == COUNT_OF_CARDS_FOR_THREE_OF_A_KIND) {
            int combinationStrength = getMostCommonValue(values).getKey();
            List<Integer> finalValues = values;
            values = values.stream()
                    .filter(v -> !v.equals(getMostCommonValue(finalValues).getKey()))
                    .collect(toList());
            if (getMostCommonValue(values).getValue() >= COUNT_OF_CARDS_FOR_PAIR) {
                hand.setCombinationStrength(combinationStrength);
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getMostCommonValue(values).getValue() >= COUNT_OF_CARDS_FOR_THREE_OF_A_KIND) {
            hand.setCombinationStrength(getMostCommonValue(values).getKey());
            setKickers(hand);
            return true;
        }
        return false;
    }

    private static boolean isTwoPair(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getMostCommonValue(values).getValue() == COUNT_OF_CARDS_FOR_PAIR) {
            hand.setCombinationStrength(getMostCommonValue(values).getKey());
            List<Integer> finalValues = values;
            values = values.stream()
                    .filter(v -> !v.equals(getMostCommonValue(finalValues).getKey()))
                    .collect(toList());
            if (getMostCommonValue(values).getValue() == COUNT_OF_CARDS_FOR_PAIR) {
                if (hand.getCombinationStrength() < getMostCommonValue(values).getKey()) {
                    hand.setCombinationStrength(getMostCommonValue(values).getKey());
                }
                setKickers(hand);
                return true;
            }
        }
        return false;
    }

    private static boolean isPair(List<Card> allCards, Hand hand) {
        List<Integer> values = getSortedValues(allCards);
        if (getMostCommonValue(values).getValue() >= COUNT_OF_CARDS_FOR_PAIR) {
            hand.setCombinationStrength(getMostCommonValue(values).getKey());
            setKickers(hand);
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

    private static Map.Entry<Integer, Long> getMostCommonValue(List<Integer> values) {
        return values.stream().collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get();
    }

    private static Map.Entry<Character, Long> getMostCommonSuit(List<Character> suits) {
        return suits.stream().collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get();
    }

    private static void setKickersForTheHighCardCombination(Hand hand, List<Card> cards) {
        List<Integer> kickers = getSortedValues(cards).stream()
                .distinct()
                .sorted(Integer::compareTo)
                .collect(toList());
        for (int counter = kickers.size() - 1; counter >= 0; counter--) {
            if (counter == 0)
                hand.setFifthKicker(kickers.get(counter));
            if (counter == 1)
                hand.setFourthKicker(kickers.get(counter));
            if (counter == 2)
                hand.setThirdKicker(kickers.get(counter));
            if (counter == 3)
                hand.setSecondKicker(kickers.get(counter));
            if (counter == 4)
                hand.setCombinationStrength(kickers.get(counter));
        }
    }

    private static void setKickers(Hand hand) {
        List<Integer> kickers = getSortedValues(hand.getCards()).stream()
                .distinct()
                .filter(v -> !v.equals(hand.getCombinationStrength()))
                .sorted(Integer::compareTo)
                .collect(toList());
        for (int counter = kickers.size() - 1; counter >= 0; counter--) {
            if (counter == 1)
                hand.setFifthKicker(kickers.get(counter));
            if (counter == 2)
                hand.setFourthKicker(kickers.get(counter));
            if (counter == 3)
                hand.setThirdKicker(kickers.get(counter));
            if (counter == 4)
                hand.setSecondKicker(kickers.get(counter));
        }
    }
}
