package team.unnamed.chairs.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.ChairHandler;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;

public class PlayerInteractListener implements Listener {

    private final ChairHandler chairHandler;
    private final ChairEntityHandler chairEntityHandler;
    private final ChairDataRegistry chairDataRegistry;

    public PlayerInteractListener(ChairHandler chairHandler,
                                  ChairEntityHandler chairEntityHandler,
                                  ChairDataRegistry chairDataRegistry) {
        this.chairHandler = chairHandler;
        this.chairEntityHandler = chairEntityHandler;
        this.chairDataRegistry = chairDataRegistry;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Player player = event.getPlayer();

        if (chairHandler.isAllowedToUse(player)) {
            if (chairHandler.isWorldDenied(block.getWorld())) {
                return;
            }

            if (chairDataRegistry.isAlreadyUsed(block.getLocation())) {
                return;
            }

            Material type = block.getType();

            if (chairHandler.isAllowedMaterial(type)) {
                ChairData chairData = ChairData.create(player, block);

                chairEntityHandler.assignArmorStand(chairData);
                chairDataRegistry.addChairRegistry(player, chairData);
            }
        }
    }

}
