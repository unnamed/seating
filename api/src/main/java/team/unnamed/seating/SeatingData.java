package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class SeatingData {

    private final WeakReference<Player> ownerReference;
    private final Material blockType;
    private final Location location;
    private final Location firstLocation;
    private int entityId;

    private SeatingData(Player owner, Material blockType,
                        Location location, Location firstLocation) {
        this.ownerReference = new WeakReference<>(owner);
        this.blockType = blockType;
        this.location = location;
        this.firstLocation = firstLocation;
    }

    public Player getOwner() {
        return ownerReference.get();
    }

    public UUID getOwnerReference() {
        return getOwner().getUniqueId();
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

    public Material getBlockType() {
        return blockType;
    }

    @Override
    public String toString() {
        return "SeatingData{" +
                "ownerId=" + ownerReference +
                ", location=" + location +
                ", entityId=" + entityId +
                '}';
    }

    public static SeatingData create(Player player, Material type, Location baseLocation) {
        return new SeatingData(player, type, baseLocation, player.getLocation());
    }

}
