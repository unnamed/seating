package team.unnamed.seating.registry;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.adapt.seat.SeatingEntityHandler;
import team.unnamed.seating.data.SeatingData;
import team.unnamed.seating.event.PlayerUseSeatEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

abstract class AbstractSeatingDataRegistry<T extends SeatingData>
        implements SeatingDataRegistry<T> {

    protected final SeatingEntityHandler entityHandler;
    private final Map<UUID, T> registries;

    public AbstractSeatingDataRegistry(SeatingEntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        this.registries = new HashMap<>();
    }

    @Override
    public Collection<T> getAllData() {
        return registries.values();
    }

    @Override
    public void addRegistry(Player player, T seatingData) {
        registries.put(player.getUniqueId(), seatingData);
    }

    @Override
    public @Nullable T getRegistry(UUID uuid) {
        return registries.get(uuid);
    }

    @Override
    public void createAndAddRegistry(Player player, Block block) {
        T seatingData = internalCreateAndAdd(player, block);
        if (seatingData != null) {
            PlayerUseSeatEvent useSeatEvent
                    = new PlayerUseSeatEvent(player, seatingData);

            Bukkit.getPluginManager().callEvent(useSeatEvent);

            if (!useSeatEvent.isCancelled()) {
                addRegistry(player, seatingData);
            }
        }
    }

    @Override
    public @Nullable T removeRegistry(UUID uuid) {
        return registries.remove(uuid);
    }

    protected abstract T internalCreateAndAdd(Player player, Block block);

}
