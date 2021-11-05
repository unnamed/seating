package team.unnamed.chairs.adapt.v1_8_R3.chair;

import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;

import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.v1_8_R3.Packets;
import team.unnamed.chairs.adapt.v1_8_R3.track.EntityTrackerRegistry;

public class ChairEntityHandler1_8_R3 implements ChairEntityHandler {

    private final EntityTrackerRegistry trackerRegistry;

    public ChairEntityHandler1_8_R3() {
        this.trackerRegistry = new EntityTrackerRegistry();
    }

    @Override
    public void showChairMount(ChairData chairData, Player player) {
        ChairUtils.spawn(chairData, player);
    }

    @Override
    public void updateChairYaw(ChairData chairData) {
        byte yaw = (byte) (chairData.getLocation().getYaw() * 256.0F / 360.0F);
        PacketPlayOutEntity.PacketPlayOutEntityLook entityLookPacket =
                new PacketPlayOutEntity.PacketPlayOutEntityLook(
                        chairData.getEntityId(),
                        yaw, (byte) 0, false
                );

        chairData.getSpectators().consumeAsPlayers(player -> Packets.send(
                player, entityLookPacket
        ));
    }

    @Override
    public void assignArmorStand(ChairData chairData) {
        EntityTrackerEntry trackerEntry = new ChairEntityTrackerEntry(chairData);
        chairData.setEntityId(ChairUtils.generateId(chairData));
        trackerRegistry.bindEntry(chairData, trackerEntry);
    }

    @Override
    public void destroyChair(ChairData chairData) {
        trackerRegistry.unbindEntry(chairData);
    }

}
