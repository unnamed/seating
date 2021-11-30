package team.unnamed.seating.adapt.v1_8_R3.material;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import team.unnamed.seating.adapt.material.MaterialChecker;

public class MaterialChecker1_8_R3 implements MaterialChecker {
    @Override
    public boolean isSlab(Block block) {
        return block.getState().getData() instanceof Step;
    }

    @Override
    public boolean isStair(Block block) {
        return block.getState().getData() instanceof Stairs;
    }

    @Override
    public boolean isCarpet(Block block) {
        return block.getType() == Material.CARPET;
    }
}
