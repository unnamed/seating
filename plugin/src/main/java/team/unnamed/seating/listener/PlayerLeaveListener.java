package team.unnamed.seating.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.seating.SeatingDataRegistry;

public class PlayerLeaveListener implements Listener {

    private final SeatingDataRegistry seatingDataRegistry;

    public PlayerLeaveListener(SeatingDataRegistry seatingDataRegistry) {
        this.seatingDataRegistry = seatingDataRegistry;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        seatingDataRegistry.removeRegistry(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        seatingDataRegistry.removeRegistry(event.getPlayer());
    }

}
