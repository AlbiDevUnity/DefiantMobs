package dev.albi.defiantmobs.commands;

import dev.albi.defiantmobs.mobs.DefiantMobModule;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(! (sender instanceof Player)) { return true; }

        for (String entityType: module.getChances().keySet()) {
            sender.sendMessage(entityType + ": " + module.getChances().get(entityType)+"%");
        }

        return  true;
    }
}
