package team.unnamed.chairs.adapt.hook;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import team.unnamed.chairs.adapt.AdaptionModule;

import java.util.HashSet;
import java.util.Set;

public class HookRegistry {

    private final Set<HookManager> hookManagers;

    public HookRegistry() {
        this.hookManagers = new HashSet<>();
    }

    public boolean isAvailableToSeat(Location location, Player player) {
        for (HookManager hookManager : hookManagers) {
            return hookManager.isAvailableToSeat(location, player);
        }
        return true;
    }

    public void addHookManager(HookManager hookManager) {
        hookManagers.add(hookManager);
    }

    public void setupHookManagers(Plugin plugin, AdaptionModule adaptionModule) {
        ConfigurationSection hookSection = plugin.getConfig().getConfigurationSection("hook");

        if (hookSection == null) {
            return;
        }

        for (String hookManagerKey : hookSection.getKeys(false)) { //possibly add more hooks.
            if (hookSection.getBoolean(hookManagerKey)) {
                HookManager hookManager = adaptionModule.getWorldGuardHookManager();
                hookManager.setup(plugin);
                addHookManager(hookManager); //temporally just worldguard
            }
        }
    }

}
