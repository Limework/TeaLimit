package net.teamonster.tealimit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin implements Listener
{
    private int breedLimit;
    private int naturalLimit;
    private int range;
    private int spawnEggLimit;
    private int spawnerLimit;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.breedLimit = getConfig().getInt("breed-limit");
        this.naturalLimit = getConfig().getInt("natural-limit");
        this.range = getConfig().getInt("range");
        this.spawnEggLimit = getConfig().getInt("spawnegg-limit");
        this.spawnerLimit = getConfig().getInt("spawner-limit");
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        switch(event.getSpawnReason())
        {
            case BREEDING:
            case EGG:
            case DISPENSE_EGG:
            {
                if(entityLimit(event.getEntity(), this.breedLimit))
                    event.setCancelled(true);
                break;
            }

            case NATURAL:
            case NETHER_PORTAL:
            {
                if(entityLimit(event.getEntity(), this.naturalLimit))
                    event.setCancelled(true);
                break;
            }

            case SPAWNER:
            {
                if(entityLimit(event.getEntity(), this.spawnerLimit))
                    event.setCancelled(true);
                break;
            }

            case SPAWNER_EGG:
            {
                if(entityLimit(event.getEntity(), this.spawnEggLimit))
                    event.setCancelled(true);
                break;
            }
        }
    }

    private boolean entityLimit(Entity entity, int limit)
    {
        List<Entity> entityList = entity.getNearbyEntities(this.range, 255.0d, this.range);
        EntityType entityType = entity.getType();
        int count = 0;

        for(Entity value : entityList)
        {
            if(value.getType() == entityType)
                count++;
        }

        return count > limit;
    }
}
