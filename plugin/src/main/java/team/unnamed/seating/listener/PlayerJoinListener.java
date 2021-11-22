package team.unnamed.seating.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.AdaptionModule;

public class PlayerJoinListener implements Listener {

    private final Plugin plugin;
    private final AdaptionModule adaptionModule;

    public PlayerJoinListener(
            Plugin plugin,
            AdaptionModule adaptionModule
    ) {
        this.plugin = plugin;
        this.adaptionModule = adaptionModule;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        adaptionModule.injectPacketHandler(plugin, "un-seats", event.getPlayer());
    }

}
