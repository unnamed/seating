package team.unnamed.chairs.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.event.PlayerDismountFakeEntityEvent;

public class PlayerDismountFakeEntityListener implements Listener {

    private final ChairEntityHandler chairEntityHandler;
    private final ChairDataRegistry chairDataRegistry;

    public PlayerDismountFakeEntityListener(ChairEntityHandler chairEntityHandler,
                                            ChairDataRegistry chairDataRegistry) {
        this.chairEntityHandler = chairEntityHandler;
        this.chairDataRegistry = chairDataRegistry;
    }

    @EventHandler
    public void onDismountFakeEntity(PlayerDismountFakeEntityEvent event) {
        Player player = event.getPlayer();
        ChairData chairData = chairDataRegistry.getRegistry(player);

        if (chairData != null) {
            player.teleport(chairData.getFirstLocation());
            chairEntityHandler.destroyChair(chairData);
            chairDataRegistry.removeChairRegistry(player);
        }
    }

}
