package team.unnamed.chairs.adapt.v1_8_R3.chair;

import net.minecraft.server.v1_8_R3.EntityTrackerEntry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.v1_8_R3.track.EntityTrackerRegistry;

public class ChairEntityHandler1_8_R3 implements ChairEntityHandler {

    private final EntityTrackerRegistry trackerRegistry;

    public ChairEntityHandler1_8_R3() {
        this.trackerRegistry = new EntityTrackerRegistry();
    }

    @Override
    public Location calculateBaseLocation(Player owner, Block block) {
        Location location = block.getLocation();
        Material material = block.getType();
        String materialName = material.name();

        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        if (materialName.contains("STAIRS")) {
            Stairs stairs = (Stairs) block.getState().getData();
            switch (stairs.getFacing()) {
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
        }

        Location ownerLocation = owner.getLocation();
        ownerLocation.setYaw(yaw);
        owner.teleport(ownerLocation);
        location.add(incrementX, 0, incrementZ);
        location.setYaw(yaw);
        return location;
    }

    @Override
    public void assignArmorStand(ChairData chairData) {
        EntityTrackerEntry trackerEntry = new ChairEntityTrackerEntry(chairData);
        chairData.setEntityId(ChairUtils.generateId(chairData));
        trackerRegistry.bindEntry(chairData, trackerEntry);
    }

    @Override
    public void destroyChair(ChairData chairData) {
        trackerRegistry.unbindEntry(chairData);
    }

}
