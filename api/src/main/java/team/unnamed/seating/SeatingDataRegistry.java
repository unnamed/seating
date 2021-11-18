package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SeatingDataRegistry {

    private final Map<UUID, SeatingData> registry;
    private final Map<String, UUID> seatsByLocation;

    public SeatingDataRegistry() {
        this.registry = new HashMap<>();
        this.seatsByLocation = new HashMap<>();
    }

    public Collection<SeatingData> getAllSeats() {
        return registry.values();
    }

    public void addRegistry(Player player, SeatingData seatingData) {
        UUID uuid = player.getUniqueId();
        registry.put(uuid, seatingData);
        seatsByLocation.put(serializeLocation(seatingData.getLocation()), uuid);
    }

    public @Nullable SeatingData getRegistry(Player player) {
        return registry.get(player.getUniqueId());
    }

    public void removeRegistry(Player player) {
        SeatingData seatingData = registry.remove(player.getUniqueId());

        if (seatingData != null) {
            seatsByLocation.remove(serializeLocation(seatingData.getLocation()));
        }
    }

    public boolean isAlreadyUsed(Location location) {
        return seatsByLocation.containsKey(serializeLocation(location));
    }

    private String serializeLocation(Location location) {
        return location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

}
