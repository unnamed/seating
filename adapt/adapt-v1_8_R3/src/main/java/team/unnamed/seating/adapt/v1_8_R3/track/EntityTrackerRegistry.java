package team.unnamed.seating.adapt.v1_8_R3.track;

import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import team.unnamed.seating.data.SeatingData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityTrackerRegistry {

    private final Map<UUID, EntityTrackerEntry> entries;

    public EntityTrackerRegistry() {
        this.entries = new HashMap<>();
    }

    public void bindEntry(SeatingData seatingData, EntityTrackerEntry entry) {
        EntityTrackerAccessor.addEntry(
                seatingData.getLocation().getWorld(), entry
        );
        entries.put(seatingData.getOwnerId(), entry);
    }

    public void unbindEntry(SeatingData seatingData) {
        EntityTrackerEntry entry = entries.remove(seatingData.getOwnerId());

        if (entry == null) {
            return;
        }

        EntityTrackerAccessor.removeEntry(
                seatingData.getLocation().getWorld(), entry
        );
    }

}
