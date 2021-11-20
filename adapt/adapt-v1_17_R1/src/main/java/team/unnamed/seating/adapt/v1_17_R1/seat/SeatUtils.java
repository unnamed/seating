package team.unnamed.seating.adapt.v1_17_R1.seat;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import team.unnamed.seating.SeatingData;

import static team.unnamed.seating.adapt.entity.SeatingHeightConstants.CARPET_HEIGHT;
import static team.unnamed.seating.adapt.entity.SeatingHeightConstants.SLAB_AND_STAIRS_HEIGHT;

public final class SeatUtils {

    private SeatUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(SeatingData seatingData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.c,
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
        } else if (materialName.contains("SLAB") || materialName.contains("STAIRS")) {
            incrementY = SLAB_AND_STAIRS_HEIGHT;
        } else {
            incrementY = CARPET_HEIGHT;
        }

        return -incrementY;
    }

    public static void destroy(SeatingData seatingData, EntityPlayer spectator) {
        spectator.b.sendPacket(new PacketPlayOutEntityDestroy(seatingData.getEntityId()));
    }

    public static void spawn(SeatingData seatingData, EntityPlayer spectator) {
        Location location = seatingData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.c,
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

        PlayerConnection playerConnection = spectator.b;
        playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        playerConnection.sendPacket(new PacketPlayOutEntityMetadata(
                armorStand.getId(), armorStand.getDataWatcher(), false
        ));

        playerConnection.sendPacket(new PacketPlayOutMount(new PacketMountSerializer(
                armorStand.getId(), seatingData.getOwner().getEntityId()
        )));
    }

}
