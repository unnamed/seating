package team.unnamed.chairs.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;

public class PlayerLeaveListener implements Listener {

    private final ChairDataRegistry chairDataRegistry;
    private final ChairEntityHandler chairEntityHandler;

    public PlayerLeaveListener(ChairDataRegistry chairDataRegistry,
                               ChairEntityHandler chairEntityHandler) {
        this.chairDataRegistry = chairDataRegistry;
        this.chairEntityHandler = chairEntityHandler;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removeChair(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeChair(event.getPlayer());
    }

    private void removeChair(Player player) {
        ChairData chairData = chairDataRegistry.getRegistry(player);
        if (chairData != null) {
            chairEntityHandler.destroyChair(chairData);
            chairDataRegistry.removeChairRegistry(player);
        }
    }

}
