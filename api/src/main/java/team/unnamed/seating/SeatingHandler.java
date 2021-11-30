package team.unnamed.seating;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface SeatingHandler {

    boolean isAllowedToUse(Player player);

    boolean isWorldDenied(World world);

    boolean isChairMaterial(Block block);

    void toggleSeating(Player player);

    void sit(Player player, Block block, boolean ignoreType);

    void crawl(Player player);

}
