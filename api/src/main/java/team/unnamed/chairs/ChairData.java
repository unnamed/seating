package team.unnamed.chairs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;

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
        Material material = block.getType();
        float yaw = player.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementY;
        double incrementZ = 0.5;

        if (material == Material.CARPET) {
            incrementY = 1.5;
        } else if (material == Material.STEP) {
            incrementY = 1.25;
        } else {
            Stairs stairs = (Stairs) block.getState().getData();
            switch (stairs.getDescendingDirection()) {
                case EAST: {
                    yaw = -90;
                    incrementX = 0.8;
                    break;
                }
                case WEST: {
                    yaw = 90;
                    incrementX = 0.2;
                    break;
                }
                case NORTH: {
                    yaw = -180;
                    incrementZ = 0.2;
                    break;
                }
                case SOUTH: {
                    yaw = 0;
                    incrementZ = 0.8;
                    break;
                }
            }

            incrementY = 1.3;
        }

        Location playerLocation = player.getLocation();
        playerLocation.setYaw(yaw);
        player.teleport(playerLocation);
        location.add(incrementX, -incrementY, incrementZ);
        location.setYaw(yaw);
        return new ChairData(player.getUniqueId(), location, player.getLocation());
    }

}
