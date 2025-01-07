package lafraouzi.dev.calculator;

import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEvaluator {
    private final Map<String, Command> plugins;

    public ExpressionEvaluator(Map<String, Command> plugins) {
        this.plugins = plugins;
    }

    public double evaluate(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Command> operators = new Stack<>();


        String regex = "(\\d+\\.\\d+|\\d+|[\\+\\-x/])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression.replaceAll("\\s+", ""));

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches("[0-9.]+")) {
                // Si c'est un nombre, on le push sur la pile des nombres
                numbers.push(Double.parseDouble(token));
            } else {
                // Si c'est un opérateur, on le trouve dans les plugins
                Command operator = findOperatorBySymbol(token);

                // Applique les opérateurs dans le bon ordre en fonction de la priorité
                while (!operators.isEmpty() && operators.peek().getPriority() >= operator.getPriority()) {
                    executeOperation(numbers, operators);
                }
                operators.push(operator);
            }
        }

        // Applique les opérateurs restants
        while (!operators.isEmpty()) {
            executeOperation(numbers, operators);
        }

        // Retourne le résultat de l'évaluation
        return numbers.pop();
    }


    private Command findOperatorBySymbol(String symbol) {
        Command operator = plugins.values().stream()
                .filter(cmd -> cmd.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Opérateur non trouvé: " + symbol + ". Opérateurs disponibles: " + plugins.keySet()));
        return operator;
    }


    private void executeOperation(Stack<Double> numbers, Stack<Command> operators) {
        Command operator = operators.pop();
        double b = numbers.pop();
        double a = numbers.pop();
        numbers.push(operator.execute(a, b));
    }
}
