package team.unnamed.seating.registry;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.data.ChairSeatingData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChairSeatingDataRegistry
        extends AbstractSeatingDataRegistry<ChairSeatingData> {

    private final Map<String, UUID> seatsByLocation;

    public ChairSeatingDataRegistry(SeatingEntityHandler entityHandler) {
        super(entityHandler);
        this.seatsByLocation = new HashMap<>();
    }

    @Override
    public void addRegistry(Player player, ChairSeatingData seatingData) {
        super.addRegistry(player, seatingData);
        entityHandler.sit(player, seatingData);
        seatsByLocation.put(
                serializeLocation(seatingData.getLocation()),
                player.getUniqueId()
        );
    }

    @Override
    public @Nullable ChairSeatingData getRegistry(Location location) {
        return getRegistry(seatsByLocation.get(serializeLocation(location)));
    }

    @Override
    public @Nullable ChairSeatingData removeRegistry(UUID uuid) {
        ChairSeatingData seatingData = super.removeRegistry(uuid);

        if (seatingData != null) {
            entityHandler.destroySit(seatingData);
            seatsByLocation.remove(serializeLocation(seatingData.getLocation()));
        }
        return seatingData;
    }

    @Override
    protected ChairSeatingData internalCreateAndAdd(Player player, Block block) {
        ChairSeatingData.Builder builder = ChairSeatingData.builder();
        entityHandler.calculateBaseLocation(player, block, builder);
        return builder.build(player);
    }

    @Override
    public @Nullable ChairSeatingData removeRegistry(Location location) {
        return removeRegistry(seatsByLocation.get(serializeLocation(location)));
    }

    @Override
    public boolean isLocationUsed(Location location) {
        return seatsByLocation.containsKey(serializeLocation(location));
    }

    private String serializeLocation(Location location) {
        return location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

}
