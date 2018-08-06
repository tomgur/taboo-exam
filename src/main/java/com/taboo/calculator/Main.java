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
            "******************************************************************************\n" ;
    private static boolean hasNext = true;
    private static Scanner scanner = new Scanner(System.in);
    private static Properties properties = new Properties();
    private static Queue<Step> steps = new ArrayDeque<>();


    public static void main(String[] args) throws IOException {
        print(WELCOME_MSG, true);


        while (true) {
            if (hasNext) {
                print("Please write an equation:",true);
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

    private static boolean areYouSure() {
        print("Are you sure that's it? (y/N)",true);
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
            if (step.isStepEvaluated()) {
                print(step.getLHS() + "=" + step.getRHS(),true);
                print("------------------------", true);
                continue;
            }

            String lhs = step.getLHS();
            String rhs = step.getRHS();

            if (isJava(rhs)) {
                double v = evaluateJava(rhs);
                print(rhs + "=" + v, true);
                properties.setProperty(lhs,String.valueOf(v));
            }
            print("------------------------", true);
        }
        print("Calculation Ended", true);
    }

    private static double evaluateJava(String value) {
        if (value.contains("++")){
            String[] split = value.split("\\+\\+");
            String property = properties.getProperty(split[1]);
            if (split[0].equals("")){
                double result = Double.parseDouble(property);
                return ++result;
            } else if (split[1].equals("")) {
                print("BUG!!!! Need to update the entry in properties with the ++", true);
                return Double.parseDouble(property);
            }
        }
        throw new UnsopportedOperationException("Unsupported Operation!","XXXXXXXXXXXX");
    }

    private static double computeDivision(String s) {
        if(!s.contains("/")){
            return Double.parseDouble(s);
        }
        String[] byDivision = s.split("/");

        for (int i = 0; i < byDivision.length; i++) {
            String s1 = byDivision[i];

        }
        return 0;
    }

    private static boolean isArithmetic(String s) {
        if(s.contains("+")){
            return true;
        }
        return false;
    }

    private static boolean isJava(String s) {
        if(s.contains("++")){
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

        if (!properties.containsKey(split[0])){
            print(split[0] + " Not found in memory. Storing",true);
            Step step = new Step(s);
            steps.add(step);
            properties.setProperty(split[0], split[1]);
            print("Stored",true);
            print(properties.toString(),true);
            return;
        }
        print(split[0] + " found in memory. Value [" + properties.getProperty(split[0]) + "]",true);
    }

    private static void print(String msg, boolean newLine) {
        if (newLine) {
            System.out.println(msg);
        } else {
            System.out.print(msg);
        }
    }
}
