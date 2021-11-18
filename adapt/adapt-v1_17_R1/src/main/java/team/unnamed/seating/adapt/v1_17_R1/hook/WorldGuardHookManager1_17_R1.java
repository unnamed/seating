package team.unnamed.seating.adapt.v1_17_R1.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.hook.HookManager;

public class WorldGuardHookManager1_17_R1
        implements HookManager {

    private StateFlag seatFlag;

    @Override
    public void setup(Plugin plugin) {
        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();

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
        return WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .createQuery()
                .testState(BukkitAdapter.adapt(location), null, seatFlag);
    }

}
