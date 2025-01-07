import lafraouzi.dev.calculator.Command;

public class MultiplicationCommand implements Command {
    @Override
    public double execute(double a, double b) {
        return a * b;
    }

    @Override
    public String getSymbol() {
        return "x";
    }

    @Override
    public String getName() {
        return "multiplication";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
