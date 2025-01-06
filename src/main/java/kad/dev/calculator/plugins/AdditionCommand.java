package kad.dev.calculator.plugins;

import kad.dev.calculator.Command;

public class AdditionCommand implements Command {
    @Override
    public double execute(double a, double b) {
        return a + b;
    }

    @Override
    public String getSymbol() {
        return "+";
    }

    @Override
    public String getName() {
        return "addition";
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
