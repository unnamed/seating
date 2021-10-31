package team.unnamed.chairs.adapt.v1_8_R3.track;

import net.minecraft.server.v1_8_R3.EntityTrackerEntry;

import team.unnamed.chairs.ChairData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityTrackerRegistry {

    private final Map<UUID, EntityTrackerEntry> entries;

    public EntityTrackerRegistry() {
        this.entries = new HashMap<>();
    }

    public void bindEntry(ChairData chairData, EntityTrackerEntry entry) {
        EntityTrackerAccessor.addEntry(
                chairData.getLocation().getWorld(), entry
        );
        entries.put(chairData.getOwnerId(), entry);
    }

    public void unbindEntry(ChairData chairData) {
        EntityTrackerEntry entry = entries.remove(chairData.getOwnerId());

        if (entry == null) {
            return;
        }

        EntityTrackerAccessor.removeEntry(
                chairData.getLocation().getWorld(), entry
        );
    }

}
