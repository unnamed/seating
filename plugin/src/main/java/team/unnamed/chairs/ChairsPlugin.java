package team.unnamed.chairs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import team.unnamed.chairs.listener.PlayerInteractListener;

public class ChairsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    @Override
    public void onDisable() {

    }

}
