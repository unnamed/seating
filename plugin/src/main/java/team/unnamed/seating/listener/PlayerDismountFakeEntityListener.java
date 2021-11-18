package team.unnamed.seating.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import team.unnamed.seating.SeatingData;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.event.PlayerDismountFakeEntityEvent;

public class PlayerDismountFakeEntityListener implements Listener {

    private final SeatingEntityHandler seatingEntityHandler;
    private final SeatingDataRegistry seatingDataRegistry;

    public PlayerDismountFakeEntityListener(SeatingEntityHandler seatingEntityHandler,
                                            SeatingDataRegistry seatingDataRegistry) {
        this.seatingEntityHandler = seatingEntityHandler;
        this.seatingDataRegistry = seatingDataRegistry;
    }

    @EventHandler
    public void onDismountFakeEntity(PlayerDismountFakeEntityEvent event) {
        Player player = event.getPlayer();
        SeatingData seatingData = seatingDataRegistry.getRegistry(player);

        if (seatingData != null) {
            player.teleport(seatingData.getFirstLocation());
            seatingEntityHandler.destroy(seatingData);
            seatingDataRegistry.removeRegistry(player);
        }
    }

}
