package com.scalabootcamp.poker;

import com.scalabootcamp.poker.model.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.scalabootcamp.poker.SortingHandsHelper.sortHandsForFiveCardDraw;
import static com.scalabootcamp.poker.SortingHandsHelper.sortHandsForTexasHoldem;
import static com.scalabootcamp.poker.model.GAME_TYPE.FIVE_CARD_DRAW;
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
                    if (!gameType.equals(FIVE_CARD_DRAW)) {
                        return TestCase.builder()
                                .gameType(gameType)
                                .cardsOnTable(cardsOnTable)
                                .hands(temp.subList(2, temp.size())).build();
                    } else {
                        return TestCase.builder()
                                .gameType(gameType)
                                .hands(temp.subList(1, temp.size())).build();
                    }
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
            case (FIVE_CARD_DRAW):
                solution = sortHandsForFiveCardDraw(testCase);
                break;
            default:
                solution = String.format("Error: Your game type '%s' is not one of these [texas-holdem, five-card-draw]", testCase.getGameType());
                break;
        }
        return solution;
    }
}
