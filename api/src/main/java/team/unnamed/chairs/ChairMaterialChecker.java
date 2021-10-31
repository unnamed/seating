package team.unnamed.chairs;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.function.Predicate;

public class ChairMaterialChecker implements Predicate<Material> {

    private final FileConfiguration configuration;

    public ChairMaterialChecker(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean test(Material material) {
        Object configuredStairs = configuration.get("stairs");

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
