package team.unnamed.seating.adapt.v1_16_R3.material;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import team.unnamed.seating.adapt.material.MaterialChecker;

public class MaterialChecker1_16_R3 implements MaterialChecker {
    @Override
    public boolean isSlab(Block block) {
        return block.getBlockData() instanceof Slab;
    }

    @Override
    public boolean isStair(Block block) {
        return block.getBlockData() instanceof Stairs;
    }
}
