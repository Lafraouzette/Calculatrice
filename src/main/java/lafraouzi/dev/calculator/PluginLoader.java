package lafraouzi.dev.calculator;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.springframework.stereotype.Component;

@Component
public class PluginLoader {
    private final Map<String, Command> loadedPlugins = new HashMap<>();
    private final String pluginsDir = "plugins/";

    public List<String> listAvailablePlugins() {
        File folder = new File(pluginsDir);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Dossier des plugins introuvable : " + pluginsDir);
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith("Command.java"));
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
                .map(file -> file.getName().replace("Command.java", ""))
                .collect(Collectors.toList());
    }

    public Command loadPlugin(String pluginName) {
        try {
            String sourceFile = pluginsDir+ "\\" + pluginName + "Command.java";
            File source = new File(sourceFile);

            if (!source.exists()) {
                throw new RuntimeException("Plugin introuvable : " + pluginName);
            }
            
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int result = compiler.run(null, null, null, sourceFile, "-d", "compiled/");
            if (result != 0) {
                throw new RuntimeException("Erreur de compilation pour " + pluginName);
            }

            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("compiled/").toURI().toURL()});
            String className = pluginName + "Command"; 
            Class<?> pluginClass = classLoader.loadClass(className);

            Command command = null;
            Object instance = pluginClass.getDeclaredConstructor().newInstance();
            if ( instance instanceof Command ) {
                command = (Command) instance;
            }
            loadedPlugins.put(pluginName.toLowerCase(), command);
            classLoader.close();

            return command;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du plugin : " + e.getMessage(), e);
        }
    }

    public Map<String, Command> getLoadedPlugins() {
        return loadedPlugins;
    }

    public void unloadPlugin(String pluginName) {
        loadedPlugins.remove(pluginName.toLowerCase());
    }
}
