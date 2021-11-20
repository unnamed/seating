package team.unnamed.seating;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.event.PlayerUseSeatEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleSeatingDataRegistry implements SeatingDataRegistry {

    private final SeatingEntityHandler entityHandler;

    private final Map<UUID, SeatingData> registry;
    private final Map<String, UUID> seatsByLocation;

    public SimpleSeatingDataRegistry(SeatingEntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        this.registry = new HashMap<>();
        this.seatsByLocation = new HashMap<>();
    }

    public Collection<SeatingData> getAllData() {
        return registry.values();
    }

    public void addRegistry(Player player, SeatingData seatingData) {
        entityHandler.create(player, seatingData);

        UUID uuid = player.getUniqueId();
        registry.put(uuid, seatingData);
        seatsByLocation.put(serializeLocation(seatingData.getLocation()), uuid);
    }

    public @Nullable SeatingData getRegistry(UUID uuid) {
        return registry.get(uuid);
    }

    public @Nullable SeatingData getRegistry(Location location) {
        return getRegistry(seatsByLocation.get(serializeLocation(location)));
    }

    @Override
    public void createAndAddRegistry(Player player, Block block) {
        Location location = entityHandler.calculateBaseLocation(player, block);

        if (location != null) {
            SeatingData seatingData = SeatingData.create(player, block.getType(), location);
            PlayerUseSeatEvent useSeatEvent
                    = new PlayerUseSeatEvent(player, seatingData);

            Bukkit.getPluginManager().callEvent(useSeatEvent);

            if (!useSeatEvent.isCancelled()) {
                addRegistry(player, seatingData);
            }
        }
    }

    @Override
    public @Nullable SeatingData removeRegistry(UUID uuid) {
        SeatingData seatingData = registry.remove(uuid);

        if (seatingData != null) {
            entityHandler.destroy(seatingData);
            seatsByLocation.remove(serializeLocation(seatingData.getLocation()));
        }

        return seatingData;
    }

    @Override
    public @Nullable SeatingData removeRegistry(Location location) {
        return removeRegistry(seatsByLocation.get(serializeLocation(location)));
    }

    public boolean isLocationUsed(Location location) {
        return seatsByLocation.containsKey(serializeLocation(location));
    }

    private String serializeLocation(Location location) {
        return location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

}
