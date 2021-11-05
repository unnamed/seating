package team.unnamed.chairs;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChairDataRegistry {

    private final Map<UUID, ChairData> registry;
    private final Map<String, UUID> chairsByLocation;

    public ChairDataRegistry() {
        this.registry = new HashMap<>();
        this.chairsByLocation = new HashMap<>();
    }

    public Collection<ChairData> getAllChairs() {
        return registry.values();
    }

    public void addChairRegistry(Player player, ChairData chairData) {
        UUID uuid = player.getUniqueId();
        registry.put(uuid, chairData);
        chairsByLocation.put(serializeLocation(chairData.getLocation()), uuid);
    }

    public @Nullable ChairData getRegistry(Player player) {
        return registry.get(player.getUniqueId());
    }

    public void removeChairRegistry(Player player) {
        ChairData chairData = registry.remove(player.getUniqueId());

        if (chairData != null) {
            chairsByLocation.remove(serializeLocation(chairData.getLocation()));
        }
    }

    public boolean isAlreadyUsed(Location location) {
        return chairsByLocation.containsKey(serializeLocation(location));
    }

    private String serializeLocation(Location location) {
        return location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

}
