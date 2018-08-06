package com.taboo.calculator;

import com.taboo.exceptions.UnsopportedOperationException;

import java.io.IOException;
import java.util.*;

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
//        test(args);
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

    public static void test(String[] args) {
//        simplify();
    }

    private static String simplify(String equation) {
        String result = "";
        Properties replacements = new Properties();

        List<String> partss = listParts(equation);
        if (isSimpleEquation(partss)) {
            return solveSimpleEquation(partss);
        }

        if (partss.size() > 1) {
            for (int i = 0; i < partss.size(); i++) {
                String part = partss.get(i);
                if (isEquation(part)) {
                    String simplify = simplify(part);
                } else {
                    return part;
                }
            }
        }
        return "BLAT!!!!";
    }

    private static String solveSimpleEquation(List<String> simplifiedEquation) {
        String result = "";
        int varA = Integer.parseInt(simplifiedEquation.get(0));
        int varB = Integer.parseInt(simplifiedEquation.get(2));
        String operator = simplifiedEquation.get(1);

        if (operator.equals("+")){
            result = String.valueOf(varA + varB);
        }

        return result;
    }

    private static String listToString(List<String> parts) {
        String result = "";
        for (int i = 0; i < parts.size(); i++) {
            result += parts.get(i);
        }
        return result;
    }

    private static List<String> listParts(String equation) {
        List<String> list = new ArrayList<>();
        String[] split = equation.split("(?=[+])|(?<=[+])");

        if (split.length % 3 == 0) {
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                int i1 = 0;
                int i2 = 0;

                if (isEquation(s)) {
                    List<String> strings = listParts(s);
                    String newQery = listToString(strings);
                    if (isNumber(newQery) || isSimpleEquation(strings)) {
                        list.add(newQery);
                        continue;
                    } else {
                        List<String> strings1 = listParts(newQery);
                        if (isSimpleEquation(strings1)) {
                            list.add(listToString(strings1));
                        }
                    }
                }

                if (isNumber(s)) {
                    list.add(s);
                    continue;
                }

                if (s.equals("+")) {
                    list.add("+");
                    continue;
                }

                if (s.equals("*")) {
                    list.add("*");
                    continue;
                } else {
                    list = listParts(split[i - 1]);
                }
                if (isNumber(split[i + 1])) {
                    list.add(split[i + 1]);
                } else {
                    List<String> strings = listParts(split[i + 1]);
                    for (String string : strings) {
                        list.add(string);
                    }
                }
                list.add(String.valueOf(i1 + i2));
            }
        } else {
            split = equation.split("(?=[*/])|(?<=[*/])");
            if (split.length % 3 == 0)
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    if (s.equals("*")) {
                        int i1 = Integer.parseInt(split[i - 1]);
                        int i2 = Integer.parseInt(split[i + 1]);
                        list.add(String.valueOf(i1 * i2));
                    }
                }
            return list;
        }
        return list;
    }

    private static boolean isSimpleEquation(List<String> list) {
        if (list.size() == 3 &&
                isNumber(list.get(0)) &&
                isOperator(list.get(1)) &&
                isNumber(list.get(2))) {
            return true;
        }
        return false;
    }

    private static boolean isOperator(String s) {
        if (s.equals("+") ||
                s.equals("-") ||
                s.equals("*") ||
                s.equals("/")) {
            return true;
        }
        return false;
    }

    private static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
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

    private static void calculateEquation(String equation) {
        String string = simplify(equation);
        print("simplified: " + string, true);

    }

    private static int evaluateJava(String value) {
        int result = 0;
        if (value.contains("++")) {
            String[] increment = value.split("\\+{2}");
            String propValue;
            String propKey = null;
            if (value.startsWith("++")) {
                // pre-increment
                propKey = increment[1];
                propValue = properties.getProperty(propKey);
                result = Integer.parseInt(propValue);
                ++result;
            } else if (value.endsWith("++")) {
                propKey = increment[1];
                propValue = properties.getProperty(increment[0]);
                result = Integer.parseInt(propValue);
            }
            properties.setProperty(propKey, String.valueOf(result));
            print(propKey + "=" + properties.getProperty(propKey), true);
        } else {
            throw new UnsopportedOperationException("Unsupported Operation!", "XXXXXXXXXXXX");
        }
        return result;
    }


    private static boolean isJavaExpression(String s) {
        if (s.contains("++")) {
            return true;
        }
        return false;
    }


    private static boolean isEquation(String s) {
        if ((s.contains("+") ||
                s.contains("-") ||
                s.contains("/") ||
                s.contains("*")) && (s.length() >= 2) &&
                !s.contains("++")) {
            return true;
        }
        return false;
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

    private static void print(String msg, boolean newLine) {
        if (newLine) {
            System.out.println(msg);
        } else {
            System.out.print(msg);
        }
    }
}
