package team.unnamed.seating.adapt.v1_16_R3.seat;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutMount;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.data.SeatingData;

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

    public static double calculateHeight(ChairSeatingData seatingData) {
        double incrementY;
        ChairSeatingData.ChairType chairType = seatingData.getChairType();

        switch (chairType) {
            case SLAB:
            case STAIR: {
                incrementY = SLAB_AND_STAIRS_HEIGHT;
                break;
            }
            default: {
                incrementY = CARPET_HEIGHT;
                break;
            }
        }

        return -incrementY;
    }

    public static void destroy(ChairSeatingData seatingData, EntityPlayer spectator) {
        spectator.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(
                seatingData.getSpigotId()
        ));
    }

    public static void spawn(ChairSeatingData seatingData, EntityPlayer spectator) {
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

        armorStand.e(seatingData.getSpigotId());
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
