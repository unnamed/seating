package team.unnamed.seating.adapt.v1_17_R1.material;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import team.unnamed.seating.adapt.material.MaterialChecker;

public class MaterialChecker1_17_R1 implements MaterialChecker {
    @Override
    public boolean isSlab(Block block) {
        return block.getBlockData() instanceof Slab;
    }

    @Override
    public boolean isStair(Block block) {
        return block.getBlockData() instanceof Stairs;
    }
}
