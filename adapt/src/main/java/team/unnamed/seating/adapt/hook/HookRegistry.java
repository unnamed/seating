package team.unnamed.seating.adapt.hook;

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
        try {
            Class.forName("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");

            HookManager hookManager = adaptionModule.getWorldGuardHookManager();
            hookManager.setup(plugin);
            addHookManager(hookManager); // temporally just worldguard
        } catch (ClassNotFoundException ignored) {
            // nothing should happen, it means that worldguard isn't installed
        }
    }

}
