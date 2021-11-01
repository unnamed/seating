package team.unnamed.chairs.adapt.v1_8_R3.hook;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import team.unnamed.chairs.adapt.hook.HookManager;

public class WorldGuardHookManager1_8_R3
        implements HookManager {

    private StateFlag chairFlag;

    @Override
    public void setup(Plugin plugin) {
        WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
        FlagRegistry flagRegistry = worldGuardPlugin.getFlagRegistry();

        Flag<?> registeredFlag = flagRegistry.get("allow-chairs");
        if (registeredFlag == null) {
            chairFlag = new StateFlag("allow-chairs", true);
            flagRegistry.register(chairFlag);
        } else {
            chairFlag = (StateFlag) registeredFlag;
        }
    }

    @Override
    public boolean isAvailableToSeat(Location location, Player player) {
        WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
        return worldGuardPlugin.getRegionManager(location.getWorld())
                .getApplicableRegions(location)
                .testState(worldGuardPlugin.wrapPlayer(player), chairFlag);
    }

}
