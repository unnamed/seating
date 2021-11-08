package team.unnamed.chairs.adapt.v1_8_R3.chair;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.v1_8_R3.track.AbstractEntityTrackerEntry;

public class ChairEntityTrackerEntry
        extends AbstractEntityTrackerEntry {

    private final ChairData chairData;
    private byte lastYaw;

    public ChairEntityTrackerEntry(ChairData chairData) {
        this.chairData = chairData;
    }

    @Override
    protected Location getLocation() {
        return chairData.getLocation();
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
        Player spectator = player.getBukkitEntity();
        ChairUtils.spawn(chairData, spectator);
    }

    @Override
    protected void hide(EntityPlayer player) {
        ChairUtils.destroy(chairData, player.getBukkitEntity());
    }

}
