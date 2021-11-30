package team.unnamed.seating.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class CrawlUtils {

    private CrawlUtils() {
        throw new UnsupportedOperationException();
    }

    public static void regenerate(Block block) {
        if (block.getType() == Material.BARRIER) {
            block.setType(Material.AIR);
        }
    }

    public static void generateBarrier(Block block) {
        if (block.getType() == Material.AIR) {
            block.setType(Material.BARRIER);
        }
    }

    public static boolean isBlockedToCrawl(Player player) {
        return player.isFlying() ||
                player.getLocation().getBlock().isLiquid() ||
                !player.isOnGround() || player.isSprinting();
    }

}
