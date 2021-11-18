package team.unnamed.seating.adapt.v1_8_R3.seat;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingData;
import team.unnamed.seating.adapt.v1_8_R3.track.AbstractEntityTrackerEntry;

public class SeatingEntityTrackerEntry
        extends AbstractEntityTrackerEntry {

    private final SeatingData seatingData;
    private byte lastYaw;

    public SeatingEntityTrackerEntry(SeatingData seatingData) {
        this.seatingData = seatingData;
    }

    @Override
    protected Location getLocation() {
        return seatingData.getLocation();
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
                        seatingData.getEntityId(),
                        yaw, (byte) 0, false
                );

        for (EntityPlayer entityPlayer : trackedPlayers) {
            entityPlayer.playerConnection.sendPacket(entityLookPacket);
        }
    }

    @Override
    protected void show(EntityPlayer player) {
        SeatUtils.spawn(seatingData, player);
    }

    @Override
    protected void hide(EntityPlayer player) {
        SeatUtils.destroy(seatingData, player);
    }

}
