package com.taboo.calculator;

public class Step {
    private String input;
    private String rhs;
    private String lhs;
    private String evaluatedRHS;


    public Step(String input) {
        this.input = input;
        this.lhs = input.split("=")[0];
        this.rhs = input.split("=")[1];
    }

    public String getLHS() {
        return lhs;
    }

    public String getRHS() {
        return rhs;
    }

    public boolean isStepEvaluated() {
        try {
            Double aDouble = Double.parseDouble(getRHS());
            evaluatedRHS = String.valueOf(aDouble);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
