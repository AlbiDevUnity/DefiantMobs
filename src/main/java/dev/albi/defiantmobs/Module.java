package dev.albi.defiantmobs;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module {

    private final String moduleName;
    protected final JavaPlugin corePlugin;

    public Module(String moduleName, JavaPlugin corePlugin){
        this.moduleName = moduleName;
        this.corePlugin = corePlugin;
    }

    public void onEnable(){
        System.out.println("Loaded " + moduleName);
    }
    public void onDisable(){
        System.out.println("unloaded " + moduleName);
    }
}
