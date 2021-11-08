package team.unnamed.chairs.adapt.v1_16_R3.chair;

import net.minecraft.server.v1_16_R3.PlayerChunkMap;
import net.minecraft.server.v1_16_R3.WorldServer;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.v1_16_R3.track.CustomEntityTracker;
import team.unnamed.chairs.adapt.v1_16_R3.track.EmptyEntity;

import static team.unnamed.chairs.adapt.v1_16_R3.track.CustomEntityTracker.ENTITY_TRACKER_FIELD;

public class ChairEntityHandler1_16_R3 implements ChairEntityHandler {

    @Override
    public Location calculateBaseLocation(Player owner, Block block) {
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();

        if (blockData instanceof Stairs) {
            Stairs stairs = (Stairs) blockData;
            switch (stairs.getFacing().getOppositeFace()) {
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
        int entityId = ChairUtils.generateId(chairData);
        chairData.setEntityId(entityId);

        Location chairLocation = chairData.getLocation();
        WorldServer worldServer = ((CraftWorld) chairLocation.getWorld()).getHandle();
        EmptyEntity emptyEntity = new EmptyEntity(worldServer);
        emptyEntity.e(entityId);

        //paper and paper forks :(
        Chunk chunk = chairLocation.getChunk();
        emptyEntity.chunkX = chunk.getX();
        emptyEntity.chunkZ = chunk.getZ();

        emptyEntity.setPosition(chairLocation.getX(), chairLocation.getY(), chairLocation.getZ());

        PlayerChunkMap chunkMap = worldServer.getChunkProvider().playerChunkMap;
        PlayerChunkMap.EntityTracker entityTracker = new CustomEntityTracker(chunkMap, emptyEntity);

        ENTITY_TRACKER_FIELD.setAccessible(true);
        try {
            ENTITY_TRACKER_FIELD.set(
                    entityTracker, new ChairEntityTrackerEntry(
                            worldServer, emptyEntity,
                            entityTracker.trackedPlayers, chairData
                    )
            );
        } catch (IllegalAccessException ignored) {
        } finally {
            ENTITY_TRACKER_FIELD.setAccessible(false);
        }

        chunkMap.trackedEntities.put(entityId, entityTracker);
    }

    @Override
    public void destroyChair(ChairData chairData) {
        PlayerChunkMap.EntityTracker entityTracker
                = ((CraftWorld) chairData.getLocation().getWorld())
                .getHandle()
                .getChunkProvider()
                .playerChunkMap.trackedEntities
                .remove(chairData.getEntityId());

        if (entityTracker != null) {
            entityTracker.a();
        }
    }

}
