package dev.albi.defiantmobs.commands;

import dev.albi.defiantmobs.mobs.DefiantMobModule;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddChanceCommand implements CommandExecutor, TabCompleter {

    private final DefiantMobModule module;

    public AddChanceCommand(DefiantMobModule module){
        this.module = module;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {

        if(! (sender instanceof Player)) { return true; }
        if(args.length < 2) { return true; }

        String entityType = args[0];
        int chance = Integer.parseInt(args[1]);

        module.getChances().put(entityType, chance);

        return  true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, Command cmd, @NonNull String label, String @NonNull [] args) {

        if(cmd.getName().equalsIgnoreCase("addChance")){
            if(args.length > 1){
                return new ArrayList<>();
            }
            List<String> list = new ArrayList<>();
            Arrays.stream(EntityType.values()).forEach(t -> list.add(t.toString().toLowerCase()));
            return  list;
        }

        return null;
    }
}
