package team.unnamed.seating.adapt.v1_16_R3.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import team.unnamed.seating.adapt.hook.HookManager;

public class WorldGuardHookManager1_16_R3
        implements HookManager {

    private StateFlag sitFlag;
    private StateFlag crawlFlag;

    @Override
    public void setup(Plugin plugin) {
        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        sitFlag = createFlag(flagRegistry, SIT_WORLDGUARD_FLAG);
        crawlFlag = createFlag(flagRegistry, CRAWL_WORLDGUARD_FLAG);
    }

    @Override
    public boolean isAvailableToSit(Location location, Player player) {
        return checkState(sitFlag, location);
    }

    @Override
    public boolean isAvailableToCrawl(Player player) {
        return checkState(crawlFlag, player.getLocation());
    }

    private boolean checkState(StateFlag flag, Location location) {
        return WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .createQuery()
                .testState(BukkitAdapter.adapt(location), null, flag);
    }

    private StateFlag createFlag(FlagRegistry flagRegistry, String name) {
        StateFlag flag;
        Flag<?> registeredFlag = flagRegistry.get(name);
        if (registeredFlag == null) {
            flag = new StateFlag(name, true);
            flagRegistry.register(sitFlag);
        } else {
            flag = (StateFlag) registeredFlag;
        }
        return flag;
    }

}
