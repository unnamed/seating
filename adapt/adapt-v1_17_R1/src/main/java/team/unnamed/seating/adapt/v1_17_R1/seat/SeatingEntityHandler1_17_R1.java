package team.unnamed.seating.adapt.v1_17_R1.seat;

import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingData;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_17_R1.track.CustomEntityTracker;
import team.unnamed.seating.adapt.v1_17_R1.track.EmptyEntity;

import static team.unnamed.seating.adapt.v1_17_R1.track.CustomEntityTracker.ENTITY_TRACKER_FIELD;

public class SeatingEntityHandler1_17_R1 implements SeatingEntityHandler {

    @Override
    public Location calculateBaseLocation(Player owner, Block block) {
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();

        if (blockData instanceof Stairs stairs) {
            switch (stairs.getFacing().getOppositeFace()) {
                case EAST -> {
                    yaw = -90;
                    incrementX = 0.8;
                }
                case WEST -> {
                    yaw = 90;
                    incrementX = 0.2;
                }
                case NORTH -> {
                    yaw = -180;
                    incrementZ = 0.2;
                }
                case SOUTH -> {
                    yaw = 0;
                    incrementZ = 0.8;
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
    public void assignArmorStand(SeatingData seatingData) {
        int entityId = SeatUtils.generateId(seatingData);
        seatingData.setEntityId(entityId);

        Location seatLocation = seatingData.getLocation();
        WorldServer worldServer = ((CraftWorld) seatLocation.getWorld()).getHandle();
        EmptyEntity emptyEntity = new EmptyEntity(worldServer);
        emptyEntity.e(entityId);

        emptyEntity.setPosition(seatLocation.getX(), seatLocation.getY(), seatLocation.getZ());

        PlayerChunkMap chunkMap = worldServer.getChunkProvider().a;
        PlayerChunkMap.EntityTracker entityTracker = new CustomEntityTracker(chunkMap, emptyEntity);

        ENTITY_TRACKER_FIELD.setAccessible(true);
        try {
            ENTITY_TRACKER_FIELD.set(
                    entityTracker, new SeatingEntityTrackerEntry(
                            worldServer, emptyEntity,
                            entityTracker.f, seatingData
                    )
            );
        } catch (IllegalAccessException ignored) {
        } finally {
            ENTITY_TRACKER_FIELD.setAccessible(false);
        }

        chunkMap.G.put(entityId, entityTracker);
    }

    @Override
    public void destroy(SeatingData seatingData) {
        PlayerChunkMap.EntityTracker entityTracker
                = ((CraftWorld) seatingData.getLocation().getWorld())
                .getHandle()
                .getChunkProvider()
                .a.G.remove(seatingData.getEntityId());

        if (entityTracker != null) {
            entityTracker.a();
        }
    }

}
