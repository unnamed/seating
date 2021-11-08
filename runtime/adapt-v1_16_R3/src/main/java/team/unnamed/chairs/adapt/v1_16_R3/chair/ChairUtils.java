package team.unnamed.chairs.adapt.v1_16_R3.chair;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutMount;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.EntitySpectators;
import team.unnamed.chairs.adapt.v1_16_R3.Packets;

import java.util.ArrayList;
import java.util.List;

import static team.unnamed.chairs.adapt.entity.ChairHeightConstants.*;

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

    public static void destroy(ChairData chairData, Player spectator) {
        chairData.getSpectators().removeSpectator(spectator);
        Packets.send(spectator, new PacketPlayOutEntityDestroy(chairData.getEntityId()));
    }

    public static void spawn(ChairData chairData, Player spectator) {
        EntitySpectators spectators = chairData.getSpectators();

        if (spectators.isSpectator(spectator)) {
            return;
        }

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

        List<Packet<?>> packets = new ArrayList<>();
        packets.add(new PacketPlayOutSpawnEntityLiving(armorStand));
        packets.add(new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), false));

        armorStand.passengers.add(((CraftPlayer) chairData.getOwner()).getHandle());
        packets.add(new PacketPlayOutMount(armorStand));

        spectators.addSpectator(spectator);

        Packets.send(spectator, packets);
    }

}
