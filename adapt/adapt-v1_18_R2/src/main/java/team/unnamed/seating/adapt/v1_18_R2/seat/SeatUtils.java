package team.unnamed.seating.adapt.v1_18_R2.seat;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import team.unnamed.seating.adapt.v1_18_R2.Packets;
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

        return armorStand.ae();
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
        Packets.send(spectator, new PacketPlayOutEntityDestroy(seatingData.getSpigotId()));
    }

    public static void spawnChair(ChairSeatingData seatingData, EntityPlayer spectator) {
        Location location = seatingData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.c,
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.b(
                location.getX(),
                location.getY() + calculateHeight(seatingData),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.e(seatingData.getSpigotId());
        armorStand.j(true); // set invisible
        armorStand.a(true); // set small

        Packets.send(
                spectator,
                new PacketPlayOutSpawnEntityLiving(armorStand),
                new PacketPlayOutEntityMetadata(
                        armorStand.ae(), armorStand.ai(), false
                ),
                new PacketPlayOutMount(new PacketMountSerializer(
                        armorStand.ae(), seatingData.getOwner().getEntityId()
                ))
        );
    }

}
