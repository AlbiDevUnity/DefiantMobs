package dev.albi.defiantmobs.mobs;

import dev.albi.defiantmobs.Module;
import dev.albi.defiantmobs.utils.DefiantMobUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DefiantMobModule extends Module implements Listener {

    @Getter
    private HashMap<String, Integer> chances;

    public DefiantMobModule(JavaPlugin corePlugin) {
        super("defiantMobModule", corePlugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        corePlugin.getServer().getPluginManager().registerEvents(this, corePlugin);

        //load
        this.chances = DefiantMobUtils.loadConfig(corePlugin.getConfig());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        HandlerList.unregisterAll(this);

        //save
        DefiantMobUtils.saveConfig(chances);
        this.chances.clear();
    }

    @EventHandler
    public void OnEntityDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player) { return; }
        if(event.getEntity() instanceof ArmorStand) { return; }
        if(event.getEntity().getKiller() == null) { return; }

        event.getDrops().clear();
        LivingEntity e = event.getEntity();

        //defiant mobs dead
        if(event.getEntity().hasMetadata("IsDefiant")) {
            e.getWorld().spawnParticle(Particle.TOTEM, e.getLocation(), 200);
            e.getWorld().playSound(e.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            return;
        }

        if(!chances.containsKey(e.getType().toString().toLowerCase())) { return; }
        int chance = ThreadLocalRandom.current().nextInt(1, 100 + 1);
        if(!(chance <= chances.get(e.getType().toString().toLowerCase()))) { return; }

        new BukkitRunnable(){
            int amount = 40;
            int i = 0;
            @Override
            public void run() {
                if(i >= 30){
                    this.cancel();
                }
                e.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, e.getLocation(), amount);
                amount += 80;
                i += 5;
            }
        }.runTaskTimerAsynchronously(corePlugin, 0L, 5L);

        corePlugin.getServer().getScheduler().runTaskLater(corePlugin, () -> {

            double maxHealth = e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 2;
            LivingEntity entity = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), e.getType());

            if(e instanceof Ageable && entity instanceof Ageable){
                Ageable a = (Ageable) entity;
                if(((Ageable) e).isAdult()){
                    a.setAdult();
                }else{
                    a.setBaby();
                }
            }

            entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, e.getLocation(), 200);
            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);

            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
            entity.setHealth(maxHealth);
            ArmorStand armorStand = (ArmorStand) entity.getPassengers().get(0);
            armorStand.setCustomName(
                    ChatColor.DARK_RED + "Defiant " + entity.getName() +
                    ChatColor.WHITE + " [" +
                    ChatColor.RED +  (int)(entity.getHealth()) +
                    ChatColor.WHITE + "/" +
                    ChatColor.RED + (int)entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +
                    ChatColor.WHITE + "]");

            entity.setMetadata("IsDefiant", new FixedMetadataValue(corePlugin, true));
        }, 60L);
    }
}
