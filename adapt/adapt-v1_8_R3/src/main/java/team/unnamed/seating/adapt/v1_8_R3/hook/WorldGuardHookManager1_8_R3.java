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

    private StateFlag sitFlag;

    @Override
    public void setup(Plugin plugin) {
        FlagRegistry flagRegistry = WorldGuardPlugin.inst().getFlagRegistry();
        sitFlag = createFlag(flagRegistry, SIT_WORLDGUARD_FLAG);
    }

    @Override
    public boolean isAvailableToSit(Location location, Player player) {
        return checkState(sitFlag, player, location);
    }

    @Override
    public boolean isAvailableToCrawl(Player player) {
        return false;
    }

    private boolean checkState(StateFlag flag, Player player, Location location) {
        WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
        return worldGuardPlugin.getRegionManager(location.getWorld())
                .getApplicableRegions(location)
                .testState(worldGuardPlugin.wrapPlayer(player), flag);
    }

    private StateFlag createFlag(FlagRegistry flagRegistry, String name) {
        StateFlag flag;
        Flag<?> registeredFlag = flagRegistry.get(name);
        if (registeredFlag == null) {
            flag = new StateFlag(name, true);
            flagRegistry.register(flag);
        } else {
            flag = (StateFlag) registeredFlag;
        }
        return flag;
    }

}
