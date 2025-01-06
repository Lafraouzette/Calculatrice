package kad.dev.calculator;


public interface Command {
    double execute(double a, double b);
    String getSymbol();
    String getName();
    int getPriority();
}
