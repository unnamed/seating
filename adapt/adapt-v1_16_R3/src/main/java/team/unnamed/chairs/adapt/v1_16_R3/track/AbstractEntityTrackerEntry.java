package team.unnamed.chairs.adapt.v1_16_R3.track;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityTrackerEntry;
import net.minecraft.server.v1_16_R3.WorldServer;

import java.util.Set;

public abstract class AbstractEntityTrackerEntry
        extends EntityTrackerEntry {

    public AbstractEntityTrackerEntry(WorldServer worldserver, Entity entity,
                                      Set<EntityPlayer> trackedPlayers) {
        super(worldserver, entity, 1, false, packet -> {}, trackedPlayers);
    }

    @Override
    public void a() {
        entityTick();
    }

    @Override
    public void a(EntityPlayer player) {
        hide(player);
    }

    @Override
    public void b(EntityPlayer player) {
        show(player);
    }

    protected abstract void entityTick();

    protected abstract void show(EntityPlayer player);

    protected abstract void hide(EntityPlayer player);

}
