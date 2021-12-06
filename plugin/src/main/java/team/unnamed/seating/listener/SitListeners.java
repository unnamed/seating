package team.unnamed.seating.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.event.PlayerDismountFakeEntityEvent;

import java.util.List;

import static team.unnamed.seating.SeatingHandler.SIT_PERMISSION;

public class SitListeners implements Listener {

    private final SeatingDataRegistry<ChairSeatingData> chairDataRegistry;
    private final SeatingHandler seatingHandler;

    public SitListeners(SeatingDataRegistry<ChairSeatingData> chairDataRegistry,
                        SeatingHandler seatingHandler) {
        this.chairDataRegistry = chairDataRegistry;
        this.seatingHandler = seatingHandler;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (chairDataRegistry.isLocationUsed(
                event.getBlock().getLocation()
                        .subtract(0, 1, 0)
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();
        BlockFace direction = event.getDirection();

        if (blocks.isEmpty()) {
            removeSeatingData(event.getBlock(), direction);
        } else {
            for (Block block : event.getBlocks()) {
                if (removeSeatingData(block, direction)) {
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onDismountFakeEntity(PlayerDismountFakeEntityEvent event) {
        Player player = event.getPlayer();
        ChairSeatingData seatingData = chairDataRegistry.removeRegistry(player);

        if (seatingData != null) {
            player.teleport(seatingData.getFirstLocation());
        }
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

        if (player.hasPermission(SIT_PERMISSION)) {
            if (player.getItemInHand().getType() != Material.AIR) {
                return;
            }

            if (seatingHandler.isInChairUseRange(player, block)) {
                seatingHandler.sit(player, block, false);
            }
        }
    }

    private boolean removeSeatingData(Block block, BlockFace direction) {
        Block nextBlock = block.getRelative(direction);
        Location location = nextBlock.getLocation().subtract(0, 1, 0);
        return chairDataRegistry.removeRegistry(location) != null;
    }

}
