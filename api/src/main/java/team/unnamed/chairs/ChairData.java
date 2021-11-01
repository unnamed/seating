package team.unnamed.chairs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChairData {

    private final UUID ownerId;
    private final Location location;
    private final Location firstLocation;
    private int entityId;

    private final EntitySpectators spectators;

    private ChairData(UUID ownerId, Location location, Location firstLocation) {
        this.ownerId = ownerId;
        this.location = location;
        this.firstLocation = firstLocation;
        this.spectators = new EntitySpectators();
    }

    public Player getOwner() {
        return Bukkit.getPlayer(ownerId);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Location getLocation() {
        return location;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public EntitySpectators getSpectators() {
        return spectators;
    }

    @Override
    public String toString() {
        return "ChairData{" +
                "ownerId=" + ownerId +
                ", location=" + location +
                ", entityId=" + entityId +
                '}';
    }

    public static ChairData create(Player player, Block block) {
        Location location = block.getLocation().clone();
        location.add(0.5, -0.5, 0.5);
        return new ChairData(player.getUniqueId(), location, player.getLocation());
    }

}
