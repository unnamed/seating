package team.unnamed.seating.adapt.v1_18_R2.track;

import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import java.util.Set;

import static team.unnamed.seating.adapt.v1_18_R2.track.CustomEntityTracker.ENTITY_TRACKER_FIELD;

public final class EntityTrackerAccessor {

    private EntityTrackerAccessor() {
        throw new UnsupportedOperationException();
    }

    public static PlayerChunkMap.EntityTracker addEntry(Location location,
                                                        int entityId,
                                                        EntityTrackerEntryCreator entryCreator) {
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        EmptyEntity emptyEntity = new EmptyEntity(worldServer);
        emptyEntity.e(entityId);
        emptyEntity.g(location.getX(), location.getY(), location.getZ());

        PlayerChunkMap chunkMap = worldServer.k().a;
        PlayerChunkMap.EntityTracker entityTracker = new CustomEntityTracker(chunkMap, emptyEntity);

        ENTITY_TRACKER_FIELD.setAccessible(true);
        try {
            ENTITY_TRACKER_FIELD.set(entityTracker, entryCreator.create(
                    worldServer, emptyEntity, entityTracker.f
            ));
        } catch (IllegalAccessException ignored) {
        } finally {
            ENTITY_TRACKER_FIELD.setAccessible(false);
        }

        chunkMap.J.put(entityId, entityTracker);
        return entityTracker;
    }

    public static void removeEntry(Location location, int entityId) {
        PlayerChunkMap.EntityTracker entityTracker
                = ((CraftWorld) location.getWorld())
                .getHandle()
                .k()
                .a.J.remove(entityId);

        if (entityTracker != null) {
            entityTracker.a();
        }
    }

    public interface EntityTrackerEntryCreator {

        EntityTrackerEntry create(WorldServer worldServer, Entity entity,
                                  Set<ServerPlayerConnection> players);

    }

}
