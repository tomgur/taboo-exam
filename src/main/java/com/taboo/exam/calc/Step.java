package taboo.exam.calc;

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

    public String getInput() {
        return input;
    }

    public String getLHS() {
        return lhs;
    }

    public String getRHS() {
        return rhs;
    }

    public void setRhs(String rhs) {
        this.rhs = rhs;
    }

    public void setLhs(String lhs) {
        this.lhs = lhs;
    }

    public boolean isStepEvaluated() {
        try {
            int i = Integer.parseInt(getRHS());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
