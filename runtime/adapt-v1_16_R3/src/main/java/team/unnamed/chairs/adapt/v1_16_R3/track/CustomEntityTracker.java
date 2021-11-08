package team.unnamed.chairs.adapt.v1_16_R3.track;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.PlayerChunkMap;

import java.lang.reflect.Field;

public class CustomEntityTracker extends PlayerChunkMap.EntityTracker {

    public static final Field ENTITY_TRACKER_FIELD;

    static {
        try {
            ENTITY_TRACKER_FIELD = PlayerChunkMap.EntityTracker.class
                    .getDeclaredField("trackerEntry");
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public CustomEntityTracker(PlayerChunkMap chunkMap, Entity entity) {
        chunkMap.super(entity, 48, 1, false);
    }

}
