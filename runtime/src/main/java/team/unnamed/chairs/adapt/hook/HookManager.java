package team.unnamed.chairs.adapt.hook;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface HookManager {

    String WORLDGUARD_FLAG = "unchairs";

    void setup(Plugin plugin);

    boolean isAvailableToSeat(Location location, Player player);

}
