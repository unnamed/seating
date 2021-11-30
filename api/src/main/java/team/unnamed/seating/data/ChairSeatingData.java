package team.unnamed.seating.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChairSeatingData extends AbstractSeatingData {

    private final ChairType chairType;
    private final Material blockType;
    private final Location firstLocation;

    protected ChairSeatingData(Player owner, Material blockType,
                               Location location, ChairType chairType,
                               Location firstLocation) {
        super(owner, location);
        this.blockType = blockType;
        this.chairType = chairType;
        this.firstLocation = firstLocation;
    }

    public Material getBlockType() {
        return blockType;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public ChairType getChairType() {
        return chairType;
    }

    @Override
    public Type getType() {
        return Type.CHAIR;
    }

    public static Builder builder() {
        return new Builder();
    }

    public enum ChairType {
        SLAB, STAIR, CARPET, BLOCK
    }

    public static class Builder {

        private Location location;
        private ChairType chairType;
        private Material blockType;

        private Builder() {

        }

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setChairType(ChairType chairType) {
            this.chairType = chairType;
            return this;
        }

        public Builder setBlockType(Material blockType) {
            this.blockType = blockType;
            return this;
        }

        public ChairSeatingData build(Player player) {
            if (location == null || blockType == null || chairType == null) {
                return null;
            }

            return new ChairSeatingData(
                    player, blockType, location,
                    chairType, player.getLocation()
            );
        }

    }

}
