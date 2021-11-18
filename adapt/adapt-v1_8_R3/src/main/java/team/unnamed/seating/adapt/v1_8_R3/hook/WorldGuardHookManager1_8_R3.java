package team.unnamed.seating.adapt.v1_8_R3.hook;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import team.unnamed.seating.adapt.hook.HookManager;

public class WorldGuardHookManager1_8_R3
        implements HookManager {

    private StateFlag seatFlag;

    @Override
    public void setup(Plugin plugin) {
        WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
        FlagRegistry flagRegistry = worldGuardPlugin.getFlagRegistry();

        Flag<?> registeredFlag = flagRegistry.get(WORLDGUARD_FLAG);
        if (registeredFlag == null) {
            seatFlag = new StateFlag(WORLDGUARD_FLAG, true);
            flagRegistry.register(seatFlag);
        } else {
            seatFlag = (StateFlag) registeredFlag;
        }
    }

    @Override
    public boolean isAvailableToSeat(Location location, Player player) {
        WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
        return worldGuardPlugin.getRegionManager(location.getWorld())
                .getApplicableRegions(location)
                .testState(worldGuardPlugin.wrapPlayer(player), seatFlag);
    }

}
