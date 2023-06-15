package dev.albi.defiantmobs.commands;

import dev.albi.defiantmobs.mobs.DefiantMobModule;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetChancesCommand implements CommandExecutor {

    private final DefiantMobModule module;

    public GetChancesCommand(DefiantMobModule module){
        this.module = module;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if(! (sender instanceof Player)) { return true; }

        for (String entityType: module.getChances().keySet()) {
            sender.sendMessage(entityType + ": " + module.getChances().get(entityType)+"%");
        }

        return  true;
    }
}
