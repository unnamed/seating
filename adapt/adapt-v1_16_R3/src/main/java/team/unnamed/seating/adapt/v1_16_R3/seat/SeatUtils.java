package team.unnamed.seating.adapt.v1_16_R3.seat;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import team.unnamed.seating.SeatingData;

import java.io.IOException;

import static team.unnamed.seating.adapt.entity.SeatingHeightConstants.CARPET_HEIGHT;
import static team.unnamed.seating.adapt.entity.SeatingHeightConstants.SLAB_AND_STAIRS_HEIGHT;

public final class SeatUtils {

    private SeatUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(SeatingData seatingData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.ARMOR_STAND,
                ((CraftWorld) seatingData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static double calculateHeight(SeatingData seatingData) {
        Material material = seatingData.getBlockType();
        String materialName = material.name();
        double incrementY;

        if (materialName.contains("CARPET")) {
            incrementY = CARPET_HEIGHT;
        } else {
            incrementY = SLAB_AND_STAIRS_HEIGHT;
        }

        return -incrementY;
    }

    public static void destroy(SeatingData seatingData, EntityPlayer spectator) {
        spectator.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(seatingData.getEntityId()));
    }

    public static void spawn(SeatingData seatingData, EntityPlayer spectator) {
        Location location = seatingData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.ARMOR_STAND,
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(),
                location.getY() + calculateHeight(seatingData),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.e(seatingData.getEntityId());
        armorStand.setNoGravity(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        PlayerConnection playerConnection = spectator.playerConnection;
        playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        playerConnection.sendPacket(new PacketPlayOutEntityMetadata(
                armorStand.getId(), armorStand.getDataWatcher(), false
        ));

        PacketPlayOutMount packetPlayOutMount = new PacketPlayOutMount();

        try {
            packetPlayOutMount.a(new PacketMountSerializer(
                    armorStand.getId(), seatingData.getOwner().getEntityId()
            ));
        } catch (IOException ignored) {
        }

        playerConnection.sendPacket(packetPlayOutMount);
    }

}
