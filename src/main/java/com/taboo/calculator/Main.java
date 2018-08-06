package com.taboo.calculator;

import com.taboo.exceptions.UnsopportedOperationException;

import java.io.IOException;
import java.util.*;

import static com.taboo.calculator.Utils.*;

public class Main {
    public static final String WELCOME_MSG =
            "******************************************************************************\n" +
            "***         Hi welcome to the Tabo**** expression calculator               ***\n" +
            "***               Please enter equations in the form of:                   ***\n" +
            "*** 'A [arithmetic operation [+-/*] OR [java operation ++ -- += /= *=] B   ***\n" +
            "***   New variables will be stored and can be called in later equations    ***\n" +
            "******************************************************************************\n";
    private static boolean hasNext = true;
    private static Scanner scanner = new Scanner(System.in);
    private static Properties properties = new Properties();
    private static Queue<Step> steps = new ArrayDeque<>();


    public static void main(String[] args) throws IOException {
        print(WELCOME_MSG, true);
        while (true) {
            if (hasNext) {
                print("Please write an equation:", true);
                addSteps();
            } else {
                if (areYouSure()) {
                    calculate();
                    break;
                }
                continue;
            }
        }
    }

    private static void addSteps() {
        String s = scanner.nextLine();

        if ("".equals(s)) {
            hasNext = false;
            return;
        }

        String[] split = s.split("=");

        if (!properties.containsKey(split[0])) {
            print(split[0] + " Not found in memory. Storing", true);
            Step step = new Step(s);
            steps.add(step);
            properties.setProperty(split[0], split[1]);
            print("Stored", true);
            print(properties.toString(), true);
            return;
        }
        print(split[0] + " found in memory. Value [" + properties.getProperty(split[0]) + "]", true);
    }

    private static boolean areYouSure() {
        print("Are you sure that's it? (y/N)", true);
        String sure = scanner.nextLine();

        if (sure.equals("y")) {
            return true;
        }
        hasNext = true;
        return false;
    }

    private static void calculate() {
        print("Starting Calculation", true);
        for (Step step : steps) {
            print("Input: " + step.getInput(), true);
            if (step.isStepEvaluated()) {
                print(step.getLHS() + "=" + step.getRHS(), true);
                print("------------------------", true);
                continue;
            }

            String leftHandSide = step.getLHS();
            String rightHandSide = step.getRHS();

            if (isEquation(rightHandSide)) {
                String simplifiedRightHandSide = simplify(rightHandSide);
                if(isNumber(simplifiedRightHandSide)){
                    properties.setProperty(leftHandSide,simplifiedRightHandSide);
                    print(leftHandSide + "=" + simplifiedRightHandSide,true);
                }else {
                    System.err.println("ERROR - element is not a number.....");
                }
            }

            if (isJavaExpression(rightHandSide)) {
                int i = evaluateJava(rightHandSide);
                print(leftHandSide + "=" + i, true);
                properties.setProperty(leftHandSide, String.valueOf(i));
            }
            print(properties.toString(),true);
            print("------------------------", true);
        }
        print("Calculation Ended", true);
    }
}
