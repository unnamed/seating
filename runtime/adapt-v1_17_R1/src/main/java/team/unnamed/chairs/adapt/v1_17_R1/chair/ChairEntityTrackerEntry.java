package team.unnamed.chairs.adapt.v1_17_R1.chair;

import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;
import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.v1_17_R1.track.AbstractEntityTrackerEntry;

import java.util.Set;

public class ChairEntityTrackerEntry extends AbstractEntityTrackerEntry {

    private final Set<ServerPlayerConnection> trackedPlayers;
    private final ChairData chairData;
    private byte lastYaw;

    public ChairEntityTrackerEntry(WorldServer worldserver, Entity entity,
                                   Set<ServerPlayerConnection> trackedPlayers, ChairData chairData) {
        super(worldserver, entity, trackedPlayers);
        this.chairData = chairData;
        this.trackedPlayers = trackedPlayers;
    }

    @Override
    protected void entityTick() {
        Player owner = chairData.getOwner();

        byte yaw = (byte) (owner.getLocation().getYaw() * 256.0F / 360.0F);

        if (yaw == lastYaw) {
            return;
        }

        lastYaw = yaw;

        PacketPlayOutEntity.PacketPlayOutEntityLook entityLookPacket =
                new PacketPlayOutEntity.PacketPlayOutEntityLook(
                        chairData.getEntityId(),
                        yaw, (byte) 0, false
                );

        for (ServerPlayerConnection playerConnection : trackedPlayers) {
            playerConnection.sendPacket(entityLookPacket);
        }
    }

    @Override
    protected void show(EntityPlayer player) {
        ChairUtils.spawn(chairData, player);
    }

    @Override
    protected void hide(EntityPlayer player) {
        ChairUtils.destroy(chairData, player);
    }

}
