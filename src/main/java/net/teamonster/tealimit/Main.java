package net.teamonster.tealimit;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    int breedLimit = getConfig().getInt("breed-limit");
    int naturalLimit = getConfig().getInt("natural-limit");
    int range = getConfig().getInt("range");
    int spawnEggLimit = getConfig().getInt("spawnegg-limit");
	int spawnerLimit = getConfig().getInt("spawner-limit");

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason().equals(SpawnReason.BREEDING) || e.getSpawnReason().equals(SpawnReason.EGG) || e.getSpawnReason().equals(SpawnReason.DISPENSE_EGG)) {
            if (entityBreedLimit(e.getEntity(), this.breedLimit)) {
                e.setCancelled(true);
            }
        } else if (e.getSpawnReason().equals(SpawnReason.NATURAL) || e.getSpawnReason().equals(SpawnReason.NETHER_PORTAL)) {
            if (entityBreedLimit(e.getEntity(), this.naturalLimit)) {
                e.setCancelled(true);
            }
		} else if (e.getSpawnReason().equals(SpawnReason.SPAWNER)) {
            if (entityBreedLimit(e.getEntity(), this.spawnerLimit)) {
                e.setCancelled(true);
            }	
        } else if (e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) && entityBreedLimit(e.getEntity(), this.spawnEggLimit)) {
            e.setCancelled(true);
        }
    }

    public boolean entityBreedLimit(Entity entity, int limit) {
        List<Entity> entityList = entity.getNearbyEntities((double) this.range, 255.0d, (double) this.range);
        EntityType entityType = entity.getType();
        int count = 0;
        for (int c = 0; c < entityList.size(); c++) {
            if (((Entity) entityList.get(c)).getType() == entityType) {
                count++;
            }
        }
        if (count > limit) {
            return true;
        }
        return false;
    }
}
