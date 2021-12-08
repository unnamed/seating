package team.unnamed.seating.adapt.v1_12_R1.seat;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutMount;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.v1_12_R1.Packets;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.data.SeatingData;

import java.io.IOException;

import static team.unnamed.seating.adapt.seat.SeatingHeightConstants.*;

public final class SeatUtils {

    private static final boolean VIA_VERSION;

    static {
        boolean viaVersion;

        try {
            Class.forName("com.viaversion.viaversion.api.ViaAPI");
            viaVersion = true;
        } catch (ClassNotFoundException ignored) {
            viaVersion = false;
        }

        VIA_VERSION = viaVersion;
    }

    private SeatUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(SeatingData seatingData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) seatingData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static double calculateHeight(ChairSeatingData seatingData, EntityPlayer spectator) {
        ChairSeatingData.ChairType chairType = seatingData.getChairType();
        if (VIA_VERSION) {
            @SuppressWarnings("unchecked") ViaAPI<Player> viaAPI =
                    (ViaAPI<Player>) Via.getAPI();
            // show with another height to players with a version minor than 1.13
            if (viaAPI.getPlayerVersion(spectator.getUniqueID()) < 393) {
                return getDecrementY(chairType, LEGACY_SLAB_AND_STAIRS_HEIGHT, LEGACY_CARPET_HEIGHT);
            }
        }
        return getDecrementY(chairType, SLAB_AND_STAIRS_HEIGHT, CARPET_HEIGHT);
    }

    private static double getDecrementY(ChairSeatingData.ChairType chairType,
                                        double slabAndStairsHeight,
                                        double carpetHeight) {
        switch (chairType) {
            case SLAB:
            case STAIR: {
                return -slabAndStairsHeight;
            }
            default: {
                return -carpetHeight;
            }
        }
    }

    public static void destroy(ChairSeatingData seatingData, EntityPlayer spectator) {
        spectator.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(
                seatingData.getSpigotId()
        ));
    }

    public static void spawn(ChairSeatingData seatingData, EntityPlayer spectator) {
        Location location = seatingData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(),
                location.getY() + calculateHeight(seatingData, spectator),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.h(seatingData.getSpigotId());
        armorStand.setNoGravity(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        PacketPlayOutMount packetPlayOutMount = new PacketPlayOutMount();

        try {
            packetPlayOutMount.a(new PacketMountSerializer(
                    armorStand.getId(), seatingData.getOwner().getEntityId()
            ));
        } catch (IOException ignored) {
            return;
        }

        Packets.send(
                spectator,
                new PacketPlayOutSpawnEntityLiving(armorStand),
                packetPlayOutMount
        );
    }

}
