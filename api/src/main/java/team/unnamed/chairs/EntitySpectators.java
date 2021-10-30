package team.unnamed.chairs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class EntitySpectators {

    private final Set<UUID> spectators;

    public EntitySpectators() {
        this.spectators = new HashSet<>();
    }

    public void addSpectator(Player spectator) {
        this.spectators.add(spectator.getUniqueId());
    }

    public void removeSpectator(Player spectator) {
        this.spectators.remove(spectator.getUniqueId());
    }

    public boolean isSpectator(Player spectator) {
        return this.spectators.contains(spectator.getUniqueId());
    }

    public void consumeAsPlayers(Consumer<Player> action) {
        Iterator<UUID> iterator = spectators.iterator();

        while (iterator.hasNext()) {
            Player spectator = Bukkit.getPlayer(iterator.next());

            if (spectator == null) {
                iterator.remove();
            } else {
                action.accept(spectator);
            }
        }
    }

}
