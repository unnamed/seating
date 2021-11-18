package team.unnamed.seating.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import team.unnamed.seating.SeatingData;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookRegistry;
import team.unnamed.seating.event.PlayerUseSeatEvent;

public class PlayerInteractListener implements Listener {

    private final SeatingHandler seatingHandler;
    private final HookRegistry hookRegistry;
    private final SeatingEntityHandler seatingEntityHandler;
    private final SeatingDataRegistry seatingDataRegistry;

    public PlayerInteractListener(SeatingHandler seatingHandler,
                                  HookRegistry hookRegistry,
                                  SeatingEntityHandler seatingEntityHandler,
                                  SeatingDataRegistry seatingDataRegistry) {
        this.seatingHandler = seatingHandler;
        this.hookRegistry = hookRegistry;
        this.seatingEntityHandler = seatingEntityHandler;
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
        Location blockLocation = block.getLocation();

        if (seatingHandler.isAllowedToUse(player)) {
            if (hookRegistry.isAvailableToSeat(blockLocation, player)) {
                if (seatingHandler.isWorldDenied(blockLocation.getWorld())) {
                    return;
                }

                if (seatingDataRegistry.isAlreadyUsed(blockLocation)) {
                    return;
                }

                SeatingData currentSeatingData = seatingDataRegistry.getRegistry(player);

                if (currentSeatingData != null) {
                    seatingEntityHandler.destroy(currentSeatingData);
                    seatingDataRegistry.removeRegistry(player);
                }

                Material type = block.getType();

                if (seatingHandler.isAllowedMaterial(type)) {
                    SeatingData seatingData = SeatingData.create(
                            player, type,
                            seatingEntityHandler.calculateBaseLocation(player, block)
                    );

                    PlayerUseSeatEvent useSeatEvent = new PlayerUseSeatEvent(
                            player, seatingData
                    );

                    Bukkit.getPluginManager().callEvent(useSeatEvent);

                    if (useSeatEvent.isCancelled()) {
                        return;
                    }

                    seatingEntityHandler.assignArmorStand(seatingData);
                    seatingDataRegistry.addRegistry(player, seatingData);
                }
            }
        }
    }

}
