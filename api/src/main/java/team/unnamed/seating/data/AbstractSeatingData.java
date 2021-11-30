package team.unnamed.seating.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.UUID;

abstract class AbstractSeatingData implements SeatingData {

    private final WeakReference<Player> ownerReference;
    private final Location location;
    private int entityId;

    protected AbstractSeatingData(Player owner, Location location) {
        this.ownerReference = new WeakReference<>(owner);
        this.location = location;
    }

    @Override
    public WeakReference<Player> getOwnerReference() {
        return ownerReference;
    }

    @Override
    public Player getOwner() {
        return ownerReference.get();
    }

    @Override
    public UUID getOwnerId() {
        return getOwner().getUniqueId();
    }

    @Override
    public int getSpigotId() {
        return entityId;
    }

    @Override
    public void setSpigotId(int spigotId) {
        this.entityId = spigotId;
    }

    @Override
    public Location getLocation() {
        return location;
    }

}
