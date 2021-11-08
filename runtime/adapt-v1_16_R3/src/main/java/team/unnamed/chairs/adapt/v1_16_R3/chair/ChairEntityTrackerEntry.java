package team.unnamed.chairs.adapt.v1_16_R3.chair;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.WorldServer;

import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.v1_16_R3.track.AbstractEntityTrackerEntry;

import java.util.Set;

public class ChairEntityTrackerEntry extends AbstractEntityTrackerEntry {

    private final Set<EntityPlayer> trackedPlayers;
    private final ChairData chairData;
    private byte lastYaw;

    public ChairEntityTrackerEntry(WorldServer worldserver, Entity entity,
                                   Set<EntityPlayer> trackedPlayers, ChairData chairData) {
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

        for (EntityPlayer entityPlayer : trackedPlayers) {
            entityPlayer.playerConnection.sendPacket(entityLookPacket);
        }
    }

    @Override
    protected void show(EntityPlayer player) {
        ChairUtils.spawn(chairData, player.getBukkitEntity());
    }

    @Override
    protected void hide(EntityPlayer player) {
        ChairUtils.destroy(chairData, player.getBukkitEntity());
    }

}
