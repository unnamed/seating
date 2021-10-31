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
        registry.put(player.getUniqueId(), chairData);
    }

    public @Nullable ChairData getRegistry(Player player) {
        return registry.get(player.getUniqueId());
    }

    public void removeChairRegistry(Player player) {
        registry.remove(player.getUniqueId());
    }

}
