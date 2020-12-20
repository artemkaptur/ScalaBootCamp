package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.Card;
import com.scalabootcamp.poker.model.TestCase;
import com.scalabootcamp.poker.model.Hand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.scalabootcamp.poker.HandStrengthEvaluator.evaluateHandStrength;
import static com.scalabootcamp.poker.model.GAME_TYPE.TEXAS_HOLDEM;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) {
        List<String> testCases;
        try {
            testCases = Files.readAllLines(Paths.get(args[0]));
            writeHandsByStrengthIntoFile(testCases, args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHandsByStrengthIntoFile(List<String> testCases, String pathToFile) throws IOException {
        List<String> solutions = getSolutionsForEachHand(testCases);
        Files.write(Paths.get(pathToFile), solutions);
    }

    private static List<String> getSolutionsForEachHand(List<String> testCasesStrings) {
        List<TestCase> testCases = testCasesStrings.stream()
                .map(testCase -> {
                    List<String> temp = Arrays.asList(testCase.split(" "));
                    String gameType = temp.get(0);
                    String cardsOnTable = temp.get(1);
                    return new TestCase(gameType, cardsOnTable, temp.subList(2, temp.size()));
                })
                .collect(toList());
        return testCases.stream().map(Main::getSolution).collect(toList());
    }

    private static String getSolution(TestCase testCase) {
        String solution;
        switch (testCase.getGameType()) {
            case (TEXAS_HOLDEM):
                solution = sortHandsForTexasHoldem(testCase);
                break;
            default:
                solution = String.format("Your game type '%s' is not one of these [texas-holdem]", testCase.getGameType());
                break;
        }
        return solution;
    }

    private static String sortHandsForTexasHoldem(TestCase c) {
        List<Card> cardsOnTable = new ArrayList<>();
        for (int i = 0; i < 10; i += 2) {
            cardsOnTable.add(new Card(c.getCardsOnTable().charAt(i), c.getCardsOnTable().charAt(i + 1)));
        }
        List<Hand> hands = c.getHands().stream()
                .map(h -> Hand.builder()
                        .cards(new ArrayList<>() {{
                            add(new Card(h.charAt(0), h.charAt(1)));
                            add(new Card(h.charAt(2), h.charAt(3)));
                        }}).build())
                .collect(toList());
        hands.forEach(h -> h.setHandStrength(evaluateHandStrength(h, cardsOnTable)));
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
}
