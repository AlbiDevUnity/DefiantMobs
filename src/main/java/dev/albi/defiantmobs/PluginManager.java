package dev.albi.defiantmobs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {
    private final Map<String, Module> plugins;

    public PluginManager(){
        this.plugins = new ConcurrentHashMap<>();
    }

    public void loadPlugin(String name, Module module){
        plugins.put(name, module);
        module.onEnable();
    }

    public void unLoadPlugin(String name){
        Module module = plugins.remove(name);
        if(module != null) { module.onDisable(); }
    }
}
