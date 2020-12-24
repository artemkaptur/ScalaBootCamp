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
        List<TestCase> testCases = testCasesStrings.parallelStream()
                .map(testCase -> {
                    List<String> testCasesSplittedBySpace = Arrays.asList(testCase.split(" "));
                    String errorMessage = getErrorMessageForTestCase(testCasesSplittedBySpace);
                    if (errorMessage.isEmpty()) {
                        String gameType = testCasesSplittedBySpace.get(0);
                        String cardsOnTable = testCasesSplittedBySpace.get(1);
                        if (!gameType.equals(FIVE_CARD_DRAW)) {
                            return TestCase.builder()
                                    .gameType(gameType)
                                    .cardsOnTable(cardsOnTable)
                                    .hands(testCasesSplittedBySpace.subList(2, testCasesSplittedBySpace.size())).build();
                        } else {
                            return TestCase.builder()
                                    .gameType(gameType)
                                    .hands(testCasesSplittedBySpace.subList(1, testCasesSplittedBySpace.size())).build();
                        }
                    } else {
                        return TestCase.builder()
                                .errorMessage(errorMessage).build();
                    }
                })
                .collect(toList());
        return testCases.parallelStream().map(Main::getSolution).collect(toList());
    }

    private static String getSolution(TestCase testCase) {
        String solution = "";
        if (testCase.getErrorMessage() == null) {
            switch (testCase.getGameType()) {
                case (TEXAS_HOLDEM):
                    solution = sortHandsForTexasHoldem(testCase);
                    break;
                case (FIVE_CARD_DRAW):
                    solution = sortHandsForFiveCardDraw(testCase);
                    break;
            }
        } else {
            solution = String.format("Error: %s", testCase.getErrorMessage());
        }
        return solution;
    }

    private static String getErrorMessageForTestCase(List<String> testCasesSplittedBySpace) {
        if (testCasesSplittedBySpace.size() < 3)
            return "Not enough data for test case, please enter <game_type>, <cards_on_the_table> (if it's texas holdem) and several <hand block>";

        if (!testCasesSplittedBySpace.get(0).equals(TEXAS_HOLDEM) && !testCasesSplittedBySpace.get(0).equals(FIVE_CARD_DRAW))
            return String.format("Your game type '%s' is not one of these [texas-holdem, five-card-draw]", testCasesSplittedBySpace.get(0));

        if (testCasesSplittedBySpace.get(1).length() != 10 && testCasesSplittedBySpace.get(0).equals(TEXAS_HOLDEM))
            return "Texas holdem <cards_on_the_table> does not consist from 5 cards";

        if (!testCasesSplittedBySpace.get(1).matches("[TJQKAdhcs0-9]+") && testCasesSplittedBySpace.get(0).equals(TEXAS_HOLDEM))
            return "Texas holdem <cards_on_the_table> does not consist from allowed symbols";

        if (testCasesSplittedBySpace.get(0).equals(FIVE_CARD_DRAW) &&
                testCasesSplittedBySpace.subList(1, testCasesSplittedBySpace.size()).stream()
                        .filter(tc -> tc.length() == 10)
                        .count() != testCasesSplittedBySpace.size() - 1)
            return "One or more Five card draw <hand block> does not consist from 5 cards";

        if (testCasesSplittedBySpace.get(0).equals(FIVE_CARD_DRAW) &&
                testCasesSplittedBySpace.subList(1, testCasesSplittedBySpace.size()).stream()
                        .filter(tc -> tc.matches("[TJQKAdhcs0-9]+"))
                        .count() != testCasesSplittedBySpace.size() - 1)
            return "One or more Five card draw <hand block> does not consist from allowed symbols";

        if (testCasesSplittedBySpace.get(0).equals(TEXAS_HOLDEM) &&
                testCasesSplittedBySpace.subList(2, testCasesSplittedBySpace.size()).stream()
                        .filter(tc -> tc.length() == 4)
                        .count() != testCasesSplittedBySpace.size() - 2)
            return "One or more Texas holdem draw <hand block> does not consist from 2 cards";

        if (testCasesSplittedBySpace.get(0).equals(TEXAS_HOLDEM) &&
                testCasesSplittedBySpace.subList(2, testCasesSplittedBySpace.size()).stream()
                        .filter(tc -> tc.matches("[TJQKAdhcs0-9]+"))
                        .count() != testCasesSplittedBySpace.size() - 2)
            return "One or more Texas holdem draw <hand block> does not consist from allowed symbols";

        return "";
    }
}
