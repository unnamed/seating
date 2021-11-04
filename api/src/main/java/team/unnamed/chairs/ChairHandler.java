package team.unnamed.chairs;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

import static team.unnamed.bukkit.ServerVersionUtils.SERVER_VERSION_INT;

public class ChairHandler {

    private final FileConfiguration configuration;

    public ChairHandler(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean isAllowedToUse(Player player) {
        return player.hasPermission(configuration.getString("chairs.permission"));
    }

    public boolean isWorldDenied(World world) {
        List<String> deniedWorldNames = configuration.getStringList("chairs.denied-worlds");
        return deniedWorldNames.contains(world.getName());
    }

    public boolean isAllowedMaterial(Material material) {
        Object configuredStairs = configuration.get("chairs.materials");
        String materialName = material.name();

        if (configuredStairs instanceof String) {
            String stairMaterialKey = (String) configuredStairs;

            if (stairMaterialKey.equals("ALL")) {
                boolean isStair = materialName.contains("STAIRS");

                if (SERVER_VERSION_INT < 13) {
                    return isStair || material == Material.STEP
                            || material == Material.WOOD_STEP
                            || material == Material.CARPET;
                } else {
                    return isStair || materialName.contains("SLAB")
                            || materialName.contains("CARPET");
                }
            } else {
                Material stairMaterial = Material.matchMaterial(stairMaterialKey);

                if (stairMaterial == null) {
                    return false;
                } else {
                    return stairMaterial == material;
                }
            }
        } else if (configuredStairs instanceof List) {
            @SuppressWarnings("unchecked") List<String> stairsMaterialKeys =
                    (List<String>) configuredStairs;

            for (String stairMaterialKey : stairsMaterialKeys) {
                if (materialName.contains(stairMaterialKey)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

}
