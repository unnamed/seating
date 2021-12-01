package team.unnamed.seating.adapt.v1_18_R1.track;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;

import java.util.Set;

public abstract class AbstractEntityTrackerEntry
        extends EntityTrackerEntry {

    public AbstractEntityTrackerEntry(WorldServer worldserver, Entity entity,
                                      Set<ServerPlayerConnection> trackedPlayers) {
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
