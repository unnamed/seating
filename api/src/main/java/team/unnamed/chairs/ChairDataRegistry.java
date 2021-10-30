package team.unnamed.chairs;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChairDataRegistry {

    private final Map<UUID, ChairData> registry;

    public ChairDataRegistry() {
        this.registry = new HashMap<>();
    }

    public void addChairRegistry(Player player, ChairData chairData) {
        this.registry.put(player.getUniqueId(), chairData);
    }

    public @Nullable ChairData getRegistry(Player player) {
        return this.registry.get(player.getUniqueId());
    }

    public void removeChairRegistry(Player player) {
        this.registry.remove(player.getUniqueId());
    }

}
