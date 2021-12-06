package team.unnamed.seating;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface SeatingHandler {

    String SIT_PERMISSION = "seating.sit";
    String CRAWL_PERMISSION = "seating.crawl";
    String SIT_TOGGLE_PERMISSION = "seating.toggle";

    boolean isInChairUseRange(Player player, Block block);

    boolean isWorldDenied(World world);

    boolean isChairMaterial(Block block);

    void toggleSeating(Player player);

    void sit(Player player, Block block, boolean ignoreType);

    void crawl(Player player);

}
