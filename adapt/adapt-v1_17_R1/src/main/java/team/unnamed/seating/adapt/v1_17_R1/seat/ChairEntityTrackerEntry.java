package team.unnamed.seating.adapt.v1_17_R1.seat;

import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.v1_17_R1.track.AbstractEntityTrackerEntry;
import team.unnamed.seating.data.ChairSeatingData;

import java.util.Set;

public class ChairEntityTrackerEntry extends AbstractEntityTrackerEntry {

    private final Set<ServerPlayerConnection> trackedPlayers;
    private final ChairSeatingData seatingData;
    private byte lastYaw;

    public ChairEntityTrackerEntry(WorldServer worldserver, Entity entity,
                                   Set<ServerPlayerConnection> trackedPlayers,
                                   ChairSeatingData seatingData) {
        super(worldserver, entity, trackedPlayers);
        this.seatingData = seatingData;
        this.trackedPlayers = trackedPlayers;
    }

    @Override
    protected void entityTick() {
        Player owner = seatingData.getOwner();

        byte yaw = (byte) (owner.getLocation().getYaw() * 256.0F / 360.0F);

        if (yaw == lastYaw) {
            return;
        }

        lastYaw = yaw;

        PacketPlayOutEntity.PacketPlayOutEntityLook entityLookPacket =
                new PacketPlayOutEntity.PacketPlayOutEntityLook(
                        seatingData.getSpigotId(),
                        yaw, (byte) 0, false
                );

        for (ServerPlayerConnection playerConnection : trackedPlayers) {
            playerConnection.sendPacket(entityLookPacket);
        }
    }

    @Override
    protected void show(EntityPlayer player) {
        SeatUtils.spawnChair(seatingData, player);
    }

    @Override
    protected void hide(EntityPlayer player) {
        SeatUtils.destroyChair(seatingData, player);
    }

}
