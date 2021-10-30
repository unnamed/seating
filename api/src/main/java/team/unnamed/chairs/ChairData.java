package team.unnamed.chairs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChairData {

    private final UUID ownerId;
    private final Location location;
    private int entityId;

    private final EntitySpectators spectators;

    public ChairData(UUID ownerId, Location location) {
        this.ownerId = ownerId;
        this.location = location;
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

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public EntitySpectators getSpectators() {
        return spectators;
    }

}
