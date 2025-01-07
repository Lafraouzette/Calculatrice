package lafraouzi.dev.calculator.plugins;

import lafraouzi.dev.calculator.Command;

public class DivisionCommand implements Command {
    @Override
    public double execute(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division par zéro");
        }
        return a / b;
    }

    @Override
    public String getSymbol() {
        return "/";
    }

    @Override
    public String getName() {
        return "division";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}