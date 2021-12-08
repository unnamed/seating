package team.unnamed.seating.adapt.v1_12_R1.track;

import net.minecraft.server.v1_12_R1.EntityTracker;
import net.minecraft.server.v1_12_R1.EntityTrackerEntry;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.lang.reflect.Field;
import java.util.Set;

public final class EntityTrackerAccessor {

    private static final Field ENTRIES_FIELD;

    static {
        try {
            ENTRIES_FIELD = EntityTracker.class.getDeclaredField("c");
            ENTRIES_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private EntityTrackerAccessor() {
        throw new UnsupportedOperationException();
    }

    public static void addEntry(World world, EntityTrackerEntry entry) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        EntityTracker tracker = worldServer.getTracker();

        try {
            @SuppressWarnings("unchecked") Set<EntityTrackerEntry> entries =
                    (Set<EntityTrackerEntry>) ENTRIES_FIELD.get(tracker);
            if (entries.add(entry)) {
                entry.scanPlayers(worldServer.players);
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    public static void removeEntry(World world, EntityTrackerEntry entry) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        EntityTracker tracker = worldServer.getTracker();

        try {
            @SuppressWarnings("unchecked") Set<EntityTrackerEntry> entries =
                    (Set<EntityTrackerEntry>) ENTRIES_FIELD.get(tracker);
            if (entries.remove(entry)) {
                entry.a();
            }
        } catch (IllegalAccessException ignored) {
        }
    }

}
