package team.unnamed.chairs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class ChairData {

    private final WeakReference<Player> ownerReference;
    private final Material blockType;
    private final Location location;
    private final Location firstLocation;
    private int entityId;

    private final EntitySpectators spectators;

    private ChairData(Player owner, Material blockType,
                      Location location, Location firstLocation) {
        this.ownerReference = new WeakReference<>(owner);
        this.blockType = blockType;
        this.location = location;
        this.firstLocation = firstLocation;
        this.spectators = new EntitySpectators();
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

    public EntitySpectators getSpectators() {
        return spectators;
    }

    @Override
    public String toString() {
        return "ChairData{" +
                "ownerId=" + ownerReference +
                ", location=" + location +
                ", entityId=" + entityId +
                '}';
    }

    public static ChairData create(Player player, Material type, Location baseLocation) {
        return new ChairData(player, type, baseLocation, player.getLocation());
    }

}
