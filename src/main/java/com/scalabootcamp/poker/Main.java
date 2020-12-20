package com.scalabootcamp.poker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> testCases;
        try {
            testCases = Files.readAllLines(Paths.get(args[0]));
            testCases.forEach(System.out::println);
            writeHandsByStrengthIntoFile(testCases, args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHandsByStrengthIntoFile(List<String> testCases, String pathToFile) throws IOException {
        List<String> solutions = getSolutionsForEachHand(testCases);
        Files.write(Paths.get(pathToFile), solutions);
    }

    private static List<String> getSolutionsForEachHand(List<String> testCases) {
        return List.of("Calculated solution");
    }
}
