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
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.data.SeatingData;

import static team.unnamed.seating.adapt.seat.SeatingHeightConstants.CARPET_HEIGHT;
import static team.unnamed.seating.adapt.seat.SeatingHeightConstants.SLAB_AND_STAIRS_HEIGHT;

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

    public static double calculateHeight(ChairSeatingData seatingData) {
        double incrementY;
        ChairSeatingData.ChairType chairType = seatingData.getChairType();

        switch (chairType) {
            case SLAB, STAIR -> incrementY = SLAB_AND_STAIRS_HEIGHT;
            default -> incrementY = CARPET_HEIGHT;
        }

        return -incrementY;
    }

    public static void destroyChair(ChairSeatingData seatingData, EntityPlayer spectator) {
        spectator.b.sendPacket(new PacketPlayOutEntityDestroy(seatingData.getSpigotId()));
    }

    public static void spawnChair(ChairSeatingData seatingData, EntityPlayer spectator) {
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

        armorStand.e(seatingData.getSpigotId());
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
