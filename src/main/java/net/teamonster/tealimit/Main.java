package net.teamonster.tealimit;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;

public class Main extends JavaPlugin implements Listener, CommandExecutor
{
    private int breedLimit;
    private int naturalLimit;
    private int range;
    private int spawnEggLimit;
    private int spawnerLimit;

    @Override
    public void onEnable()
    {
        Bukkit.getConsoleSender().sendMessage("TeaLimit Enabled");
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getCommand("tealimit").setExecutor(this);

        this.breedLimit = getConfig().getInt("breed-limit");
        this.naturalLimit = getConfig().getInt("natural-limit");
        this.range = getConfig().getInt("range");
        this.spawnEggLimit = getConfig().getInt("spawnegg-limit");
        this.spawnerLimit = getConfig().getInt("spawner-limit");
    }

    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage("TeaLimit Disabled");
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
    @EventHandler
    public void onPreCreatureSpawn(PreCreatureSpawnEvent event)
    {
        switch(event.getReason())
        {
            case BREEDING:
            case EGG:
            case DISPENSE_EGG:
            {
                if(entityLimit(event.getSpawnLocation(), this.breedLimit, event.getType()))
                    event.setCancelled(true);
                break;
            }

            case NATURAL:
            case NETHER_PORTAL:
            {
                if(entityLimit(event.getSpawnLocation(), this.naturalLimit, event.getType()))
                    event.setCancelled(true);
                break;
            }

            case SPAWNER:
            {
                if(entityLimit(event.getSpawnLocation(), this.spawnerLimit, event.getType()))
                    event.setCancelled(true);
                break;
            }

            case SPAWNER_EGG:
            {
                if(entityLimit(event.getSpawnLocation(), this.spawnEggLimit, event.getType()))
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
    private boolean entityLimit(Location location, int limit, EntityType entityType)
    {
        Collection<Entity> entityList = location.getWorld().getNearbyEntities(location, this.range, 255.0d, this.range);
        int count = 0;

        for(Entity value : entityList)
        {
            if(value.getType() == entityType)
                count++;
        }

        return count > limit;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("tealimit")){
            if(args.length == 0){
                sender.sendMessage("Invalid argument!");
                return false;
            }
            if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
                sender.sendMessage("Reloading Configuration");
                reloadConfig();
                sender.sendMessage("Configuration Reloaded");
                return true;
            }
        }
        return false;
    }
}
