package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface SeatingDataRegistry {

    Collection<SeatingData> getAllData();

    @Nullable SeatingData getRegistry(UUID playerId);

    default @Nullable SeatingData getRegistry(Player player) {
        return getRegistry(player.getUniqueId());
    }

    @Nullable SeatingData getRegistry(Location location);

    void addRegistry(Player player, SeatingData seatingData);

    void createAndAddRegistry(Player player, Block block);

    @Nullable SeatingData removeRegistry(UUID playerId);

    default @Nullable SeatingData removeRegistry(Player player) {
        return removeRegistry(player.getUniqueId());
    }

    @Nullable SeatingData removeRegistry(Location location);

    boolean isLocationUsed(Location location);

}
