package team.unnamed.chairs.adapt.v1_16_R3.chair;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import team.unnamed.chairs.ChairData;

import java.io.IOException;

import static team.unnamed.chairs.adapt.entity.ChairHeightConstants.CARPET_HEIGHT;
import static team.unnamed.chairs.adapt.entity.ChairHeightConstants.SLAB_AND_STAIRS_HEIGHT;

public final class ChairUtils {

    private ChairUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(ChairData chairData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.ARMOR_STAND,
                ((CraftWorld) chairData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static double calculateHeight(ChairData chairData) {
        Material material = chairData.getBlockType();
        String materialName = material.name();
        double incrementY;

        if (materialName.contains("CARPET")) {
            incrementY = CARPET_HEIGHT;
        } else {
            incrementY = SLAB_AND_STAIRS_HEIGHT;
        }

        return -incrementY;
    }

    public static void destroy(ChairData chairData, EntityPlayer spectator) {
        spectator.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(chairData.getEntityId()));
    }

    public static void spawn(ChairData chairData, EntityPlayer spectator) {
        Location location = chairData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.ARMOR_STAND,
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(),
                location.getY() + calculateHeight(chairData),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.e(chairData.getEntityId());
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
                    armorStand.getId(), chairData.getOwner().getEntityId()
            ));
        } catch (IOException ignored) {
        }

        playerConnection.sendPacket(packetPlayOutMount);
    }

}
