package team.unnamed.seating.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.adapt.hook.HookRegistry;
import team.unnamed.seating.user.UserToggleSeatingManager;

public class PlayerInteractListener implements Listener {

    private final SeatingHandler seatingHandler;
    private final HookRegistry hookRegistry;
    private final UserToggleSeatingManager userToggleSeatingManager;
    private final SeatingDataRegistry seatingDataRegistry;

    public PlayerInteractListener(SeatingHandler seatingHandler,
                                  HookRegistry hookRegistry,
                                  UserToggleSeatingManager userToggleSeatingManager,
                                  SeatingDataRegistry seatingDataRegistry) {
        this.seatingHandler = seatingHandler;
        this.hookRegistry = hookRegistry;
        this.userToggleSeatingManager = userToggleSeatingManager;
        this.seatingDataRegistry = seatingDataRegistry;
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
        Material type = block.getType();
        Location blockLocation = block.getLocation();

        if (seatingHandler.isAllowedToUse(player)) {
            if (!seatingHandler.isAllowedMaterial(type)) {
                return;
            }

            if (userToggleSeatingManager.hasSeatingEnabled(player)) {
                if (player.getItemInHand().getType() != Material.AIR) {
                    return;
                }

                if (hookRegistry.isAvailableToSeat(blockLocation, player)) {
                    if (seatingHandler.isWorldDenied(blockLocation.getWorld())) {
                        return;
                    }

                    if (block.getRelative(BlockFace.UP).getType() != Material.AIR) {
                        return;
                    }

                    if (seatingDataRegistry.isLocationUsed(blockLocation)) {
                        return;
                    }

                    seatingDataRegistry.removeRegistry(player);
                    seatingDataRegistry.createAndAddRegistry(player, block);
                }
            }
        }
    }

}
