package dev.albi.defiantmobs.mobs;

import dev.albi.defiantmobs.Module;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ShowHealthModule extends Module implements Listener {
    public ShowHealthModule(JavaPlugin corePlugin) {
        super("showHealthModule", corePlugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        corePlugin.getServer().getPluginManager().registerEvents(this, corePlugin);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void OnEntitySpawn(EntitySpawnEvent event){
        if(event.getEntity() instanceof Player) { return; }
        if(event.getEntity() instanceof ArmorStand) { return; }
        if(! (event.getEntity() instanceof LivingEntity)) { return; }

        LivingEntity e = (LivingEntity) event.getEntity();

        ArmorStand armorStand = event.getLocation().getWorld().spawn(event.getLocation(), ArmorStand.class);
        armorStand.setCustomName(
                ChatColor.WHITE + e.getName() +
                        ChatColor.WHITE + " [" +
                        ChatColor.RED + (int) (e.getHealth()) +
                        ChatColor.WHITE + "/" +
                        ChatColor.RED + (int) e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +
                        ChatColor.WHITE + "]");
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);

        e.addPassenger(armorStand);
    }

    @EventHandler
    public void OnEntityDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) { return; }
        if(event.getEntity() instanceof ArmorStand) { return; }
        if(! (event.getEntity() instanceof LivingEntity)) { return; }

        LivingEntity e = (LivingEntity) event.getEntity();
        ArmorStand armorStand = (ArmorStand) e.getPassengers().get(0);
        armorStand.setCustomName(e.hasMetadata("IsDefiant") ?
                ChatColor.DARK_RED + "Defiant " + e.getName() +
                        ChatColor.WHITE + " [" +
                        ChatColor.RED + (int) (e.getHealth() - event.getFinalDamage()) +
                        ChatColor.WHITE + "/" +
                        ChatColor.RED + (int) e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +
                        ChatColor.WHITE + "]"
                :
                ChatColor.WHITE + e.getName() +
                        ChatColor.WHITE + " [" +
                        ChatColor.RED + (int) (e.getHealth() - event.getFinalDamage()) +
                        ChatColor.WHITE + "/" +
                        ChatColor.RED + (int) e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +
                        ChatColor.WHITE + "]");
    }

    @EventHandler
    public void OnEntityDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player) { return; }
        if(event.getEntity() instanceof ArmorStand) { return; }
        if(!event.getEntity().getPassengers().isEmpty()){
            event.getEntity().getPassengers().get(0).remove();
        }
    }
}
