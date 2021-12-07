package team.unnamed.seating.adapt.material;

import org.bukkit.block.Block;

public interface MaterialChecker {

    boolean isSlab(Block block);

    boolean isStair(Block block);

    default boolean isAir(Block block) {
        return block.getType().isAir();
    }

    default boolean isCarpet(Block block) {
        return block.getType().name().contains("CARPET");
    }

}
