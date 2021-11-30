package team.unnamed.seating.adapt.hook;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface HookManager {

    String SIT_WORLDGUARD_FLAG = "sit";
    String CRAWL_WORLDGUARD_FLAG = "crawl";

    void setup(Plugin plugin);

    boolean isAvailableToSit(Location location, Player player);

    boolean isAvailableToCrawl(Player player);

}
