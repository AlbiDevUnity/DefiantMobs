package dev.albi.defiantmobs.utils;

import dev.albi.defiantmobs.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DefiantMobUtils {
    public static HashMap<String, Integer> loadConfig(FileConfiguration config) {
        //use bukkit runnable async
        HashMap<String, Integer> chances = new HashMap<>();

        Core.getInstance().saveDefaultConfig();

        new BukkitRunnable(){

            @Override
            public void run() {
                if(config.getConfigurationSection("chances") == null) { return; }

                for (String value: config.getConfigurationSection("chances").getKeys(false)) {
                    chances.put(value.toLowerCase(), config.getInt("chances." + value));
                }
            }
        }.runTaskAsynchronously(Core.getInstance());
        return chances;
    }

    public static void saveConfig(HashMap<String, Integer> chances) {
        //use a bukkit runnable async

        FileConfiguration config = Core.getInstance().getConfig();

        if(config.getConfigurationSection("chances") != null){
            for (String entityType: config.getConfigurationSection("chances").getKeys(false)) {
                if(chances.containsKey(entityType)) { continue; }
                config.set("chances." + entityType, null);
            }
        }
        for (String key: chances.keySet()) {
            config.set("chances." + key.toLowerCase(), chances.get(key));
        }

        Core.getInstance().saveConfig();
    }
}
