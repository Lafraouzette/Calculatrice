package lafraouzi.dev.calculator.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lafraouzi.dev.calculator.Command;
import lafraouzi.dev.calculator.ExpressionEvaluator;
import lafraouzi.dev.calculator.PluginLoader;

@Controller
public class CalculatorController {
    private final PluginLoader pluginLoader;
    private ExpressionEvaluator evaluator;

    public CalculatorController(PluginLoader pluginLoader) {
        this.pluginLoader = pluginLoader;
        this.evaluator = new ExpressionEvaluator(pluginLoader.getLoadedPlugins());
    }

    @GetMapping("/debug")
    @ResponseBody
    public String debugPlugins() {
        return pluginLoader.getLoadedPlugins().keySet().toString();
    }
    @GetMapping("/")
    public String calculator(Model model) {
        model.addAttribute("plugins", pluginLoader.listAvailablePlugins());
        return "calculator";
    }

    @PostMapping("/loadPlugin")
    @ResponseBody
    public String loadPlugin(@RequestParam String pluginName) {
        try {
            Command plugin = pluginLoader.loadPlugin(pluginName);
            return plugin.getSymbol();
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    @PostMapping("/evaluate")
    @ResponseBody
    public double evaluate(@RequestParam String expression) {
        System.out.println("controller exp = "+expression);
        return evaluator.evaluate(expression);
    }
}
