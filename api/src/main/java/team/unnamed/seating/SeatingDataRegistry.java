package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.seating.data.SeatingData;

import java.util.Collection;
import java.util.UUID;

public interface SeatingDataRegistry<T extends SeatingData> {

    Collection<T> getAllData();

    @Nullable T getRegistry(UUID playerId);

    default @Nullable T getRegistry(Player player) {
        return getRegistry(player.getUniqueId());
    }

    @Nullable T getRegistry(Location location);

    default boolean isRegistered(Player player) {
        return getRegistry(player) != null;
    }

    void addRegistry(Player player, T seatingData);

    void createAndAddRegistry(Player player, Block block);

    @Nullable T removeRegistry(UUID playerId);

    default @Nullable T removeRegistry(Player player) {
        return removeRegistry(player.getUniqueId());
    }

    @Nullable T removeRegistry(Location location);

    boolean isLocationUsed(Location location);

}
