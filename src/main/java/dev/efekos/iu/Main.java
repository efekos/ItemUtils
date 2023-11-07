package dev.efekos.iu;

import dev.efekos.iu.events.EntityEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        EntityEvents a = new EntityEvents();
        getServer().getPluginManager().registerEvents(a,this);
        getServer().getLogger().info("loaded events");

        a.TICK.runTaskTimer(this,0,1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
