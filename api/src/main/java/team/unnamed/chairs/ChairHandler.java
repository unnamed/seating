package team.unnamed.chairs;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

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

        if (configuredStairs instanceof String) {
            String stairMaterialKey = (String) configuredStairs;

            if (stairMaterialKey.equals("ALL")) {
                return material.name().contains("STAIRS");
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

            return stairsMaterialKeys.contains(material.name());
        } else {
            return false;
        }
    }

}
