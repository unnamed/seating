package team.unnamed.seating.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import team.unnamed.seating.SeatingData;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;

public class PlayerLeaveListener implements Listener {

    private final SeatingDataRegistry seatingDataRegistry;
    private final SeatingEntityHandler seatingEntityHandler;

    public PlayerLeaveListener(SeatingDataRegistry seatingDataRegistry,
                               SeatingEntityHandler seatingEntityHandler) {
        this.seatingDataRegistry = seatingDataRegistry;
        this.seatingEntityHandler = seatingEntityHandler;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removeSeat(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeSeat(event.getPlayer());
    }

    private void removeSeat(Player player) {
        SeatingData seatingData = seatingDataRegistry.getRegistry(player);
        if (seatingData != null) {
            seatingEntityHandler.destroy(seatingData);
            seatingDataRegistry.removeRegistry(player);
        }
    }

}
