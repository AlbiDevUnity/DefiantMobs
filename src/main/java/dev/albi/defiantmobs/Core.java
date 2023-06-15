package dev.albi.defiantmobs;

import dev.albi.defiantmobs.commands.AddChanceCommand;
import dev.albi.defiantmobs.commands.GetChancesCommand;
import dev.albi.defiantmobs.commands.RemoveChanceCommand;
import dev.albi.defiantmobs.mobs.DefiantMobModule;
import dev.albi.defiantmobs.mobs.ShowHealthModule;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Getter
    private static Core instance;
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        instance = this;

        pluginManager = new PluginManager();

        DefiantMobModule defiantMobModule = new DefiantMobModule((this));

        pluginManager.loadPlugin("defiantMobModule", defiantMobModule);
        pluginManager.loadPlugin("showHealthModule", new ShowHealthModule(this));

        getCommand("addChance").setExecutor(new AddChanceCommand(defiantMobModule));
        getCommand("removeChance").setExecutor(new RemoveChanceCommand(defiantMobModule));
        getCommand("getChances").setExecutor(new GetChancesCommand(defiantMobModule));
    }

    @Override
    public void onDisable() {
        //unload modules
        pluginManager.unLoadPlugin("defiantMobModule");
        pluginManager.unLoadPlugin("showHealthModule");
    }

}
