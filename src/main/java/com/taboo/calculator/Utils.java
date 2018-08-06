package com.taboo.calculator;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String simplify(String equation) {
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

    public static List<String> listParts(String equation) {
        List<String> list = new ArrayList<>();
        String[] split = equation.split("(?=[+])|(?<=[+])");

        if (split.length % 3 == 0) {
            for (int i = 0; i < split.length; i++) {
                String s = split[i].trim();
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

                if (isOperator(s)) {
                    list.add(s);
                    continue;
                } else {
                    list = listParts(split[i - 1]);
                }
                if (isNumber(split[i + 1])) {
                    list.add(split[i + 1]);
                } else {
                    List<String> strings = listParts(split[i + 1]);
                    list.addAll(strings);
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
    public static boolean isSimpleEquation(List<String> list) {
        if (list.size() == 3 &&
                isNumber(list.get(0)) &&
                isOperator(list.get(1)) &&
                isNumber(list.get(2))) {
            return true;
        }
        return false;
    }
    public static String solveSimpleEquation(List<String> simplifiedEquation) {
        String result = "";
        int varA = Integer.parseInt(simplifiedEquation.get(0));
        int varB = Integer.parseInt(simplifiedEquation.get(2));
        String operator = simplifiedEquation.get(1);

        if (operator.equals("+")){
            result = String.valueOf(varA + varB);
        }

        return result;
    }
    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static boolean isOperator(String s) {
        if (s.equals("+") ||
                s.equals("-") ||
                s.equals("*") ||
                s.equals("/")) {
            return true;
        }
        return false;
    }
    public static boolean isJavaExpression(String s) {
        if (s.contains("++")) {
            return true;
        }
        return false;
    }
    public static boolean isEquation(String s) {
        if ((s.contains("+") ||
                s.contains("-") ||
                s.contains("/") ||
                s.contains("*")) && (s.length() >= 2) &&
                !s.contains("++")) {
            return true;
        }
        return false;
    }
    public static String listToString(List<String> parts) {
        String result = "";
        for (int i = 0; i < parts.size(); i++) {
            result += parts.get(i);
        }
        return result;
    }
    public static void print(String msg, boolean newLine) {
        if (newLine) {
            System.out.println(msg);
        } else {
            System.out.print(msg);
        }
    }
}
