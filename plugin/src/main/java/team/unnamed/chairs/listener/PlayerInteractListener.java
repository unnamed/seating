package team.unnamed.chairs.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import team.unnamed.chairs.adapt.hook.HookRegistry;
import team.unnamed.chairs.event.PlayerMountChairEvent;

public class PlayerInteractListener implements Listener {

    private final ChairHandler chairHandler;
    private final HookRegistry hookRegistry;
    private final ChairEntityHandler chairEntityHandler;
    private final ChairDataRegistry chairDataRegistry;

    public PlayerInteractListener(ChairHandler chairHandler,
                                  HookRegistry hookRegistry,
                                  ChairEntityHandler chairEntityHandler,
                                  ChairDataRegistry chairDataRegistry) {
        this.chairHandler = chairHandler;
        this.hookRegistry = hookRegistry;
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
        Location blockLocation = block.getLocation();

        if (chairHandler.isAllowedToUse(player)) {
            if (hookRegistry.isAvailableToSeat(blockLocation, player)) {
                if (chairHandler.isWorldDenied(blockLocation.getWorld())) {
                    return;
                }

                if (chairDataRegistry.isAlreadyUsed(blockLocation)) {
                    return;
                }

                ChairData currentChairData = chairDataRegistry.getRegistry(player);

                if (currentChairData != null) {
                    chairEntityHandler.destroyChair(currentChairData);
                    chairDataRegistry.removeChairRegistry(player);
                }

                Material type = block.getType();

                if (chairHandler.isAllowedMaterial(type)) {
                    ChairData chairData = ChairData.create(
                            player, type,
                            chairEntityHandler.calculateBaseLocation(player, block)
                    );

                    PlayerMountChairEvent mountChairEvent = new PlayerMountChairEvent(
                            player, chairData
                    );

                    Bukkit.getPluginManager().callEvent(mountChairEvent);

                    if (mountChairEvent.isCancelled()) {
                        return;
                    }

                    chairEntityHandler.assignArmorStand(chairData);
                    chairDataRegistry.addChairRegistry(player, chairData);
                }
            }
        }
    }

}
