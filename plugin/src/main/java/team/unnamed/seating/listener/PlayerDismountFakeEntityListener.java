package team.unnamed.seating.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.seating.SeatingData;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.event.PlayerDismountFakeEntityEvent;

public class PlayerDismountFakeEntityListener implements Listener {

    private final SeatingDataRegistry seatingDataRegistry;

    public PlayerDismountFakeEntityListener(SeatingDataRegistry seatingDataRegistry) {
        this.seatingDataRegistry = seatingDataRegistry;
    }

    @EventHandler
    public void onDismountFakeEntity(PlayerDismountFakeEntityEvent event) {
        Player player = event.getPlayer();
        SeatingData seatingData = seatingDataRegistry.removeRegistry(player);

        if (seatingData != null) {
            player.teleport(seatingData.getFirstLocation());
        }
    }

}
