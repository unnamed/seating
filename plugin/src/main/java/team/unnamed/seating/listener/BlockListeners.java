package team.unnamed.seating.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import team.unnamed.seating.SeatingDataRegistry;

import java.util.List;

public class BlockListeners implements Listener {

    private final SeatingDataRegistry dataRegistry;

    public BlockListeners(SeatingDataRegistry dataRegistry) {
        this.dataRegistry = dataRegistry;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (dataRegistry.isLocationUsed(
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

    private boolean removeSeatingData(Block block, BlockFace direction) {
        Block nextBlock = block.getRelative(direction);
        Location location = nextBlock.getLocation().subtract(0, 1, 0);
        return dataRegistry.removeRegistry(location) != null;
    }

}
