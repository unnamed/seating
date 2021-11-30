package team.unnamed.seating.adapt.hook;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import team.unnamed.seating.adapt.AdaptionModule;

import java.util.HashSet;
import java.util.Set;

public class HookRegistry {

    private final Set<HookManager> hookManagers;

    public HookRegistry() {
        this.hookManagers = new HashSet<>();
    }

    public boolean isAvailableToSit(Location location, Player player) {
        for (HookManager hookManager : hookManagers) {
            return hookManager.isAvailableToSit(location, player);
        }
        return true;
    }

    public boolean isAvailableToCrawl(Player player) {
        for (HookManager hookManager : hookManagers) {
            return hookManager.isAvailableToCrawl(player);
        }
        return true;
    }

    public void addHookManager(HookManager hookManager) {
        hookManagers.add(hookManager);
    }

    public void setupHookManagers(Plugin plugin, AdaptionModule adaptionModule) {
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            HookManager hookManager = adaptionModule.getWorldGuardHookManager();
            hookManager.setup(plugin);
            addHookManager(hookManager); // temporally just worldguard
        }
    }

}
