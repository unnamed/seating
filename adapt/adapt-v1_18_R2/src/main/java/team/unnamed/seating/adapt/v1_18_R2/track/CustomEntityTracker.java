package team.unnamed.seating.adapt.v1_18_R2.track;

import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Field;

public class CustomEntityTracker extends PlayerChunkMap.EntityTracker {

    public static final Field ENTITY_TRACKER_FIELD;

    static {
        try {
            ENTITY_TRACKER_FIELD = PlayerChunkMap.EntityTracker.class
                    .getDeclaredField("b");
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public CustomEntityTracker(PlayerChunkMap chunkMap, Entity entity) {
        chunkMap.super(entity, 48, 1, false);
    }

}
